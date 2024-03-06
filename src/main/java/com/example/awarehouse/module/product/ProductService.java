package com.example.awarehouse.module.product;

import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.group.WarehouseGroupService;
import com.example.awarehouse.module.product.dto.*;
import com.example.awarehouse.module.product.mapper.ProductMapper;
import com.example.awarehouse.module.product.mapper.ProductWarehouseMapper;
import com.example.awarehouse.module.storage.FileSystemStorageService;
import com.example.awarehouse.module.storage.StorageService;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WarehouseService;
import com.example.awarehouse.module.warehouse.WorkerWarehouseService;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTierService;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WarehouseNotExistException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.awarehouse.module.warehouse.util.WarehouseConstants.WAREHOUSE_NOT_EXIST;

@Service
@AllArgsConstructor
public class ProductService {
private ProductRepository productRepository;
private WarehouseService warehouseService;
private WarehouseGroupService warehouseGroupService;
private ShelveTierService tierService;
private ProductWarehouseService productWarehouseService;
private ProductWarehouseRepository productWarehouseRepository;
private WorkerWarehouseService workerWarehouseService;
private final StorageService storageService =  new FileSystemStorageService();

    @Transactional
    public ProductDto createProduct(MultipartFile file, ProductCreationDto productDto) {
        Product savedProduct = product(productDto);
       List<ProductWarehouseDto> productWarehouses = createProductWarehouseAssociations(savedProduct, productDto.getProductWarehouses());
        setGroupAmountIfNotExist(savedProduct, productWarehouses);
        if(file!=null && !file.isEmpty()){
            saveFile(file, savedProduct);
        }
        return ProductMapper.toDto(savedProduct, productWarehouses);
    }
    void saveFile(MultipartFile file, Product savedProduct) {
        String newFileName = file.getOriginalFilename()+"_"+savedProduct.getId();
        savedProduct.setPhoto(file.getOriginalFilename());
        storageService.store(file, newFileName);
    }
    private Product product(ProductCreationDto productDto) {
        checkIfWarehouseIdAndGroupIdNotNull(productDto);
        WarehouseGroup group = getGroup(productDto.getGroupId(), productDto.getAmountGroup());
        Product product = ProductMapper.toProduct(productDto, group);
        return productRepository.save(product);
    }
    private void checkIfWarehouseIdAndGroupIdNotNull(ProductCreationDto productDto){
        int productWarehouseSize = 0;
        if(productDto.getProductWarehouses()!=null) {
            productWarehouseSize = productDto.getProductWarehouses().size();
        }
        UUID groupId = productDto.getGroupId();
        if(productWarehouseSize ==0 && groupId== null){
            throw new IllegalArgumentException("WarehouseId or groupId must be provided");
        }
    }

    private WarehouseGroup getGroup(UUID groupId, Double amountGroup) {
        if(groupId==null){
            return null;
        }
        if (groupId != null && amountGroup == null) {
            throw new IllegalArgumentException("AmountGroup must be provided");
        }
        return warehouseGroupService.getGroup(groupId).orElse(null);
    }

    private List<ProductWarehouseDto> createProductWarehouseAssociations(Product savedProduct, List<ProductWarehouseCreationDto> providedProductWarehouses){
        List<ProductWarehouseDto> productWarehouses= new ArrayList<>();
        for (ProductWarehouseCreationDto productWarehouseCreationDto :
                providedProductWarehouses) {
            ProductWarehouse productWarehouse = createProductWarehouseAssociation(productWarehouseCreationDto, savedProduct);
            productWarehouses.add(ProductWarehouseMapper.toDto(productWarehouse));
        }
        return productWarehouses;
    }
    private ProductWarehouse createProductWarehouseAssociation(ProductWarehouseCreationDto productWarehouseCreationDto, Product product) {
        UUID warehouseId = productWarehouseCreationDto.warehouseId();
        Warehouse warehouse = warehouseService.getWarehouse(warehouseId).orElseThrow(()-> new WarehouseNotExistException(WAREHOUSE_NOT_EXIST));
        ShelveTier tier = getShelveTier(productWarehouseCreationDto, product);
        ProductWarehouse productWarehouse = new ProductWarehouse(product, warehouse,  productWarehouseCreationDto.amount(), tier);
        productWarehouseRepository.save(productWarehouse);
        return productWarehouse;
    }

    private ShelveTier getShelveTier(ProductWarehouseCreationDto productWarehouse, Product product){
        if(shelveProvided(productWarehouse)) {
            ShelveTier shelveTier = tierService.getShelveTier(productWarehouse.warehouseId(), productWarehouse.shelfNumber(), productWarehouse.tierNumber());
            double volume = product.getDimensions().getVolume()* productWarehouse.amount();
            shelveTier.addOccupiedVolume(volume);
            return shelveTier;
        }
        else {
            return null;
        }
    }

    private boolean shelveProvided(ProductWarehouseCreationDto productWarehouseCreationDto) {
        if(productWarehouseCreationDto.shelfNumber() != null && productWarehouseCreationDto.tierNumber() != null){
            return true;
        }
        else {

            return false;
        }
    }

    private void setGroupAmountIfNotExist(Product savedProduct, List<ProductWarehouseDto> productWarehouses){
        if(savedProduct.getAmount()!=0){
            Double amount = productWarehouses.stream().mapToDouble(ProductWarehouseDto::amount).sum();
            savedProduct.setAmount(amount);
        }

    }

    public Page<ProductDto> getProductsFromWarehouse(UUID warehouseId, Pageable pageable){
        workerWarehouseService.validateWorkerWarehouseRelation(warehouseId);
        return productWarehouseRepository.getAllProductsByWarehouseId(pageable, warehouseId).map(ProductMapper::toDto);
    }

    public Page<ProductDto> getProductsFromWarehouses(FilterDto filterDto, Pageable pageable) {
        Map<FilterField, SortDirection> sortConditions =  filterDto.getSortConditions().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> FilterField.fromString(entry.getKey()),
                        entry -> SortDirection.fromString(entry.getValue())
                ));
        Map<FilterField, String> searchConditions =  filterDto.getSearchConditions().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> FilterField.fromString(entry.getKey()),
                        Map.Entry::getValue
                ));
        workerWarehouseService.validateWorkerWarehouseRelation(filterDto.getWarehouseIds());
        Specification<ProductWarehouse> spec = Specification
                .where(ProductWarehouseSpecification.orderBy(sortConditions))
                .and(ProductWarehouseSpecification.containsLike(searchConditions))
                .and(ProductWarehouseSpecification.hasWarehouseIn(filterDto.getWarehouseIds()));
        Page<ProductDto> productPage = productWarehouseRepository.findAll(spec, pageable).map(ProductMapper::toDto);
        if(addingProductsWithOnlyGroupIsRequired(filterDto)){
            List<ProductDto> products = addProductsWithOnlyGroup( productPage, sortConditions);
            return new PageImpl<>(
                    products,
                    PageRequest.of(
                            productPage.getNumber(),
                            productPage.getSize()),
                    productPage.getTotalElements()
            );
        }
        return productPage;
    }
    private boolean addingProductsWithOnlyGroupIsRequired(FilterDto filterDto){
        return filterDto.getGroupIds()!=null && !filterDto.getGroupIds().isEmpty();
    }
    private List<ProductDto> addProductsWithOnlyGroup( Page<ProductDto> productPage, Map<FilterField, SortDirection> sortConditions){
        Set<UUID> productIds= productPage.stream().map(ProductDto::getId).collect(Collectors.toSet());
        List<ProductDto> products= new ArrayList<>(productPage.getContent());
        ListIterator<ProductDto> iterator = products.listIterator();
        if(sortConditions.get(FilterField.AMOUNT) == null || sortConditions.get(FilterField.AMOUNT).equals(SortDirection.ASC)){
            while (iterator.hasNext()) {
                ProductDto current = iterator.next();
                if ( productIds.contains(current.getId())) {
                    iterator.previous();
                    iterator.add(ProductMapper.toProductDtoWithOnlyGroup(current));
                    iterator.next();
                    productIds.remove(current.getId());
                }
            }
        }
        else {
            while (iterator.hasPrevious()) {
                ProductDto current = iterator.previous();
                ProductDto next = iterator.next();
                if ( next.getId() != current.getId() && productIds.contains(current.getId())) {
                    iterator.add(ProductMapper.toProductDtoWithOnlyGroup(current));
                    iterator.previous();
                    productIds.remove(current.getId());
                }
            }
        }
        return products;
    }

//    private List<ProductDto> convertToProductDtoList(List<ProductWarehouse> productsPage) {
//        Iterator<ProductWarehouse> iterator = productsPage.iterator();
//        List<ProductDto> products = new ArrayList<>();
//        while (!productsPage.isEmpty()) {
//            ProductWarehouse productWarehouse = iterator.next();
//            ProductDto product = ProductMapper.toDto(productWarehouse);
//            products.add(product);
//            iterator.remove();
//            findOtherProductWarehouses(iterator, product, products);
//            iterator = productsPage.iterator();
//        }
//        return products;
//    }

//    private void findOtherProductWarehouses(Iterator<ProductWarehouse> iterator, ProductDto productDto, List<ProductDto> products) {
//        while (iterator.hasNext()) {
//            ProductWarehouse next = iterator.next();
//            if (next.getProduct().getId().equals(productDto.getId())) {
//                productDto.getProductWarehouses().add(ProductWarehouseMapper.toDto(next));
//                iterator.remove();
//            }
//        }
//    }

    public List<Product> findProductUnderstockByGroupId(UUID groupId) {
        return productRepository.findUnderstockByGroup(groupId);
    }

    public List<Product> findProductUnderstockByWarehouseId(UUID warehouseId) {
        return productWarehouseRepository.findUnderstockByWarehouse(warehouseId);
    }

    @Transactional
    public void moveProducts(MoveProductsDto moveProductsDto) {
        ShelveTier tier =tierService.getShelveTier(moveProductsDto.getWarehouseId(), moveProductsDto.getShelfNumber(), moveProductsDto.getTierNumber());
        productWarehouseService.moveProductsToTier(moveProductsDto.getProductWarehouseMoveInfos(), tier);
    }

    public void deleteProducts(DeleteProductsDto deleteProductsDto) {
        productWarehouseService. removeProductWarehousesByProductIds(deleteProductsDto.getProductIds());
        productWarehouseService.removeProductWarehouses(deleteProductsDto.getProductWarehouseIds());
        productRepository.deleteProductsById(deleteProductsDto.getProductIds());
    }

//    @Transactional
//    public ProductDto modifyProduct(ProductDto productDto) {
//        Product product = productRepository.findById(productDto.getId()).orElseThrow(()-> new ProductNotExistException("Product with id "+productDto.getId()+" not exist"));
//        product.setTitle(productDto.getTitle());
//        product.setPrice(ProductMapper.toPrice(productDto.getPrice()));
//        product.setPhoto(productDto.getPhoto());
//        List<ProductWarehouse> newProductWarehouses = new ArrayList<>();
//        for(ProductWarehouseDto productWarehouse : productDto.getProductWarehouses()){
//            if(productWarehouse.productWarehouseId()==null)
//            {
//                ProductWarehouse newProductWarehouse = createProductWarehouseAssociation(ProductWarehouseCreationDto.builder()
//                        .warehouseId(productWarehouse.warehouseId())
//                        .amount(productWarehouse.amount())
//                        .shelfNumber(productWarehouse.shelfNumber())
//                        .tierNumber(productWarehouse.tierNumber())
//                        .build(), product);
//                newProductWarehouses.add(newProductWarehouse);
//            }
//        }
//      //  List<UUID> productwarehousesIds = productDto.getProductWarehouses().stream().map(ProductWarehouseDto::productWarehouseId).collect(Collectors.toList());
//     //   List<ProductWarehouse> toSetProductWarehouses = productWarehouseRepository.findAllById(productwarehousesIds);
////       if(productDto.getProductWarehouses().size() != toSetProductWarehouses.size()){
////           for(UUID id: productwarehousesIds){
////               if(!toSetProductWarehouses.contains(id)){
////                   toSetProductWarehouses.add()
////               }
////           }
////       }
////
////        Set<ProductWarehouse> actualProductWarehouses = product.getProductWarehouses();
////        for(ProductWarehouse pw:  toSetProductWarehouses){
////            if(!actualProductWarehouses.contains(pw)){
////
////                productWarehouseRepository.save()
////            }
////        }
//        if(volumeDiffer(product, productDto)){
//                }
//        product.setDimensions(DimensionsMapper.toDimensions(productDto.getDimensionsDto()));
//        product.setAmount(productDto.getAmount());
//        product.setGroup(warehouseGroupService.getGroup(productDto.getGroup().getId()).orElse(null));
//
//        productRepository.save(product);
//        return productDto;
//    }

//    private boolean volumeDiffer(Product product, ProductDto productDto){
//        DimensionsDto dimensionsDto= productDto.getDimensionsDto();
//        double productVolume= product.getDimensions().getVolume() * product.getAmount();
//        double dtoVolume = dimensionsDto.height()*dimensionsDto.length()*dimensionsDto.width() * productDto.getAmount();
//        return productVolume != dtoVolume;
//    }
}

