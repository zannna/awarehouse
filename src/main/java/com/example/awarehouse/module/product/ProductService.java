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
import com.example.awarehouse.module.warehouse.shelve.Shelve;
import com.example.awarehouse.module.warehouse.shelve.ShelveService;
import com.example.awarehouse.module.warehouse.shelve.mapper.DimensionsMapper;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTierService;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WarehouseNotExistException;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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
private ShelveService shelveService;
private CSVProductService csvProductService;
private Validator validator;

    @Transactional
    public ProductDto createProduct(MultipartFile file, ProductCreationDto productDto) {
        validator.validate(productDto);
        Product savedProduct = product(productDto);
       List<ProductWarehouseDto> productWarehouses = createProductWarehouseAssociations(savedProduct, productDto.getProductWarehouses());
        setGroupAmountIfNotExist(savedProduct, productWarehouses);
        if(file!=null && !file.isEmpty()){
            saveFile(file, savedProduct);
        }
        return ProductMapper.toDto(savedProduct, productWarehouses);
    }
    void saveFile(MultipartFile file, Product savedProduct) {
        if(file!=null && !file.isEmpty()) {
            String newFileName = file.getOriginalFilename() + "_" + savedProduct.getId();
            savedProduct.setPhoto(file.getOriginalFilename());
            storageService.store(file, newFileName);
        }
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
        ShelveTier tier = processAddingToShelveTier(productWarehouseCreationDto, product);
        ProductWarehouse productWarehouse = new ProductWarehouse(product, warehouse,  productWarehouseCreationDto.amount(), tier);
        productWarehouseRepository.save(productWarehouse);
        return productWarehouse;
    }

    private ShelveTier processAddingToShelveTier(ProductWarehouseCreationDto productWarehouse, Product product){
        if(shelveProvided(productWarehouse)) {
            ShelveTier shelveTier = tierService.getShelveTier(productWarehouse.warehouseId(), productWarehouse.shelfNumber(), productWarehouse.tierNumber());
            if(shelveTier.isSize()){
                double volume = product.getDimensions().getVolume()* productWarehouse.amount();
                shelveTier.addOccupiedVolume(volume);   
            }
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
        Map<ProductWarehouseFilterField, SortDirection> sortConditions =  filterDto.getSortConditions().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> ProductWarehouseFilterField.fromString(entry.getKey()),
                        entry -> SortDirection.fromString(entry.getValue())
                ));
        Map<ProductWarehouseFilterField, String> searchConditions =  filterDto.getSearchConditions().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> ProductWarehouseFilterField.fromString(entry.getKey()),
                        Map.Entry::getValue
                ));
        workerWarehouseService.validateWorkerWarehouseRelation(filterDto.getWarehouseIds());
        Specification<ProductWarehouse> spec = Specification
                .where(ProductWarehouseSpecification.orderBy(sortConditions))
                .and(ProductWarehouseSpecification.containsLike(searchConditions))
                .and(ProductWarehouseSpecification.hasWarehouseIn(filterDto.getWarehouseIds()));

        Page<ProductDto> productPage = productWarehouseRepository.findAll(spec, pageable).map(ProductMapper::toDto);
        return productPage;
    }

    private List<ProductDto> addProductsWithOnlyGroup( Page<ProductDto> productPage, Map<ProductWarehouseFilterField, SortDirection> sortConditions){
        Set<UUID> productIds= productPage.stream().map(ProductDto::getId).collect(Collectors.toSet());
        List<ProductDto> products= new ArrayList<>(productPage.getContent());
        ListIterator<ProductDto> iterator = products.listIterator();
        if(sortConditions.get(ProductWarehouseFilterField.AMOUNT) == null || sortConditions.get(ProductWarehouseFilterField.AMOUNT).equals(SortDirection.ASC)){
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

    public List<Product> findProductUnderstockByGroupId(UUID groupId) {
        return productRepository.findUnderstockByGroup(groupId);
    }

    public List<Product> findProductUnderstockByWarehouseId(UUID warehouseId) {
        return productWarehouseRepository.findUnderstockByWarehouse(warehouseId);
    }

    @Transactional
    public void moveProducts(MoveProductsDto moveProductsDto) {
        validator.validate(moveProductsDto);
        ShelveTier tier =tierService.getShelveTier(moveProductsDto.getWarehouseId(), moveProductsDto.getShelfNumber(), moveProductsDto.getTierNumber());
        productWarehouseService.moveProductsToTier(moveProductsDto.getProductWarehouseMoveInfos(), tier);
    }

    public void deleteProducts(DeleteProductsDto deleteProductsDto) {
        productWarehouseService. removeProductWarehousesByProductIds(deleteProductsDto.getProductIds());
        productWarehouseService.removeProductWarehouses(deleteProductsDto.getProductWarehouseIds());
        productRepository.deleteProductsById(deleteProductsDto.getProductIds());
    }

    public Page<RowWithProducts> getProductByTier(UUID warehouseId, Pageable pageable) {
        workerWarehouseService.validateWorkerWarehouseRelation(warehouseId);
        Page<Shelve> allShelves = shelveService.getShelvesFromWarehouse(warehouseId,  pageable);
        Set<ShelveTier> tiers = allShelves.getContent().stream()
                .flatMap(shelve -> shelve.getShelveTiers().stream())
                .collect(Collectors.toSet());
        List<ProductWarehouse> productWarehouses = productWarehouseRepository. findAllByTierIn(tiers);
        Map<ShelveTier, List<Product>> groupedByTier = productWarehouses.stream()
                .collect(Collectors.groupingBy(
                        ProductWarehouse::getTier,
                        Collectors.mapping(ProductWarehouse::getProduct, Collectors.toList())
                ));

        List<RowWithProducts> shelves= ProductMapper.toShelfWithProductsDtoList(groupedByTier, allShelves.getContent());
        return new PageImpl<>(shelves, pageable, allShelves.getTotalElements());
    }

    @Transactional
    public ProductDto updateProduct(ProductDto productDto, MultipartFile file) {
        Product product = productRepository.findById(productDto.getId()).orElseThrow(()-> new IllegalArgumentException("Product with id "+productDto.getId()+" not exist"));
        Optional<ProductWarehouse> optionalProductWarehouse = product
                .getProductWarehouses()
                .stream()
                .filter(pw->pw.getId().equals(productDto.getProductWarehouses().get(0).productWarehouseId()))
                .findFirst();
        verifyIfWorkerCanChangeProduct(product, productDto, optionalProductWarehouse);
        changeDataAssociateWithProductWarehouse( optionalProductWarehouse, product, productDto);
        if(productDto.getGroup()!=null && productDto.getGroup().id()!=product.getGroup().getId()){
            changeGroup(productDto, product);
        }
        product.setTitle(productDto.getTitle());
        product.setPrice(ProductMapper.toPrice(productDto.getPrice()));
        changePhoto(file, product);
        ProductWarehouse productWarehouse = optionalProductWarehouse.orElse(null);
        productRepository.save(product);
      return ProductMapper.toDto(product, List.of(ProductWarehouseMapper.toDto(productWarehouse)));

    }
    private void verifyIfWorkerCanChangeProduct(Product product, ProductDto productDto, Optional<ProductWarehouse> optionalProductWarehouse){
        boolean isWorkerConnectedWithGroup = true;
        if(product.getGroup()!=null) {
             isWorkerConnectedWithGroup = warehouseGroupService.isWorkerWithGroupConnected(product.getGroup());
        }
        boolean isWithWarehouseConnected = optionalProductWarehouse.isPresent();
        if(isWithWarehouseConnected) {
            isWithWarehouseConnected = workerWarehouseService.validateWorkerWarehouseRelation(optionalProductWarehouse.get());
        }
        if(!isWithWarehouseConnected && !isWorkerConnectedWithGroup){
            throw new IllegalArgumentException("Worker is not connected with group and warehouse");
        }
    }
    private void changeDataAssociateWithProductWarehouse(Optional<ProductWarehouse>  optionalProductWarehouse, Product product, ProductDto productDto){
        ProductWarehouse productWarehouse = optionalProductWarehouse.orElse(null);
        if( productWarehouse!=null) {
            double oldVolume = product.getDimensions().getVolume() * productWarehouse.getNumberOfProducts();
            double newVolume = productDto.getDimensions().height() * productDto.getDimensions().length() * productDto.getDimensions().width() *
                    productDto.getAmountGroup();
            if(oldVolume!=newVolume) {
                productWarehouse.getTier().addOccupiedVolume(newVolume - oldVolume);
            }
            product.setDimensions(DimensionsMapper.toDimensions(productDto.getDimensions()));
            product.setAmount(product.getAmount()+productDto.getAmountGroup()-productWarehouse.getNumberOfProducts());
            productWarehouse.setNumberOfProducts(productDto.getAmountGroup());
            productWarehouseRepository.save(productWarehouse);
        }
        else {
            product.setDimensions(DimensionsMapper.toDimensions(productDto.getDimensions()));
            product.setAmount(productDto.getAmountGroup());
        }
    }

    private void changeGroup(ProductDto productDto, Product product){
        WarehouseGroup group =warehouseGroupService.getGroup(productDto.getGroup().id()).orElseThrow(()-> new IllegalArgumentException("Group with id "+productDto.getGroup().id()+" not exist"));
        boolean isWithNewGroupConnected = warehouseGroupService.isWorkerWithGroupConnected(group);
        if(!isWithNewGroupConnected){
            throw new IllegalArgumentException("Worker is not connected with group");
        }
        product.setGroup(group);
    }
    private  void changePhoto(MultipartFile file, Product product){
        if(file!=null && !file.isEmpty()){
            saveFile(file, product);
        }
        else if(product.getPhoto()==null) {
            storageService.delete(product.getPhotoFullName());
        }
    }
    boolean isOpacityChanged(ProductDto productDto, Product product){
        double newVolume = productDto.getDimensions().height()*productDto.getDimensions().length()*productDto.getDimensions().width();
        return newVolume != product.getDimensions().getVolume();
    }


    public Page<ProductDto> getProductsByGroup(FilterDto filterDto, Pageable pageable) {
        Map<ProductFilterField, SortDirection> sortConditions =  filterDto.getSortConditions().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> ProductFilterField.fromString(entry.getKey()),
                        entry -> SortDirection.fromString(entry.getValue())
                ));
        Map<ProductFilterField, String> searchConditions =  filterDto.getSearchConditions().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> ProductFilterField.fromString(entry.getKey()),
                        Map.Entry::getValue
                ));
        warehouseGroupService.validateWorkerGroupRelation(filterDto.getGroupIds());
        Specification<Product> spec = Specification
                .where(ProductSpecification.orderBy(sortConditions))
                .and(ProductSpecification.containsLike(searchConditions))
                .and(ProductSpecification.hasGroupIn(filterDto.getGroupIds()));

        Page<ProductDto> productPage = productRepository.findAll(spec, pageable).map(ProductMapper::toDto);
        return productPage;
    }
    @Transactional
    public ProductImportResult importProductsFromCSVFile(UUID warehouseId, MultipartFile file) throws IOException {
        AtomicInteger savedRecords = new AtomicInteger(0);
        AtomicInteger allRecords = new AtomicInteger(0);
        AtomicInteger duplicatedItems = new AtomicInteger(0);
        try {
            csvProductService.processCSVRecords(warehouseId, file, savedRecords, allRecords, duplicatedItems);
        } catch (IOException e) {
            throw new IOException("An error occurred while importing CSV file " + e.getMessage());
        } catch (MultipartException e) {
            return new ProductImportResult(0, 0, 0);
        }
        return new ProductImportResult(allRecords.get(), savedRecords.get(), duplicatedItems.get());
    }

}

