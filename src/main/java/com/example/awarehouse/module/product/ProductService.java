package com.example.awarehouse.module.product;

import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.group.WarehouseGroupService;
import com.example.awarehouse.module.product.dto.*;
import com.example.awarehouse.module.product.mapper.ProductMapper;
import com.example.awarehouse.module.product.mapper.ProductWarehouseMapper;
import com.example.awarehouse.module.product.util.exception.ProductNotExistException;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WarehouseService;
import com.example.awarehouse.module.warehouse.WorkerWarehouseService;
import com.example.awarehouse.module.warehouse.shelve.Dimensions;
import com.example.awarehouse.module.warehouse.shelve.dto.DimensionsDto;
import com.example.awarehouse.module.warehouse.shelve.mapper.DimensionsMapper;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTierService;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WarehouseNotExistException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

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
private ProductWarehouseRepository productWarehouseRepository;
private WorkerWarehouseService workerWarehouseService;

    @Transactional
    public ProductDto createProduct(ProductCreationDto productDto) {
        Product savedProduct = product(productDto);
        List<ProductWarehouseDto> productWarehouses = createProductWarehouseAssociations(savedProduct, productDto.getProductWarehouses());
        setGroupAmountIfNotExist(savedProduct, productWarehouses);
        return ProductMapper.toDto(savedProduct, productWarehouses);
    }
private Product product(ProductCreationDto productDto) {
    checkIfWarehouseIdAndGroupIdNotNull(productDto);
    WarehouseGroup group = getGroup(productDto.getGroupId(), productDto.getAmountGroup());
    Product product = ProductMapper.toProduct(productDto, group);
    return productRepository.save(product);
}
    private void checkIfWarehouseIdAndGroupIdNotNull(ProductCreationDto productDto){
       int productWarehouseSize = productDto.getProductWarehouses().size();
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
            ShelveTier shelveTier = tierService.getShelveTier(productWarehouse.warehouseId(), productWarehouse.shelveNumber(), productWarehouse.tierNumber());
            double volume = product.getDimensions().getVolume()* productWarehouse.amount();
            shelveTier.addOccupiedVolume(volume);
            return shelveTier;
        }
        else {
            return null;
        }
    }

    private boolean shelveProvided(ProductWarehouseCreationDto productWarehouseCreationDto) {
        if(productWarehouseCreationDto.shelveNumber() != null && productWarehouseCreationDto.tierNumber() != null){
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

    public Page<ProductDto> getProductsFromWarehouse(UUID warehouseId, Pageable pageable) {
        workerWarehouseService.validateWorkerWarehouseRelation(warehouseId);
        Page<ProductDto> products = productWarehouseRepository.getAllProductsByWarehouseId(pageable, warehouseId).map(ProductMapper::toDto);
        return products;
    }

    public List<ProductDto> getProductsFromWarehouses(List<UUID> warehouseIds, Pageable pageable) {
        workerWarehouseService.validateWorkerWarehouseRelation(warehouseIds);
        List<ProductWarehouse> productsPage = productWarehouseRepository. getAllProductsFromWarehouses(pageable, warehouseIds);
        List<ProductDto> products = convertToProductDtoList(productsPage);
        return products;
    }

    private List<ProductDto> convertToProductDtoList(List<ProductWarehouse> productsPage) {
        Iterator<ProductWarehouse> iterator = productsPage.iterator();
        List<ProductDto> products = new ArrayList<>();
        while (!productsPage.isEmpty()) {
            ProductWarehouse productWarehouse = iterator.next();
            ProductDto product = ProductMapper.toDto(productWarehouse);
            products.add(product);
            iterator.remove();
            findOtherProductWarehouses(iterator, product, products);
            iterator = productsPage.iterator();
        }
        return products;
    }

    private void findOtherProductWarehouses(Iterator<ProductWarehouse> iterator, ProductDto productDto, List<ProductDto> products) {
        while (iterator.hasNext()) {
            ProductWarehouse next = iterator.next();
            if (next.getProduct().getId().equals(productDto.getId())) {
                productDto.getProductWarehouses().add(ProductWarehouseMapper.toDto(next));
                iterator.remove();
            }
        }
    }

    public List<Product> findProductUnderstockByGroupId(UUID groupId) {
        return productRepository.findUnderstockByGroup(groupId);
    }

    public List<Product> findProductUnderstockByWarehouseId(UUID warehouseId) {
        return productWarehouseRepository.findUnderstockByWarehouse(warehouseId);
    }

    @Transactional
    public ProductDto modifyProduct(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId()).orElseThrow(()-> new ProductNotExistException("Product with id "+productDto.getId()+" not exist"));
        product.setTitle(productDto.getTitle());
        product.setPrice(ProductMapper.toPrice(productDto.getPrice()));
        product.setPhoto(productDto.getPhoto());
        List<ProductWarehouse> newProductWarehouses = new ArrayList<>();
        for(ProductWarehouseDto productWarehouse : productDto.getProductWarehouses()){
            if(productWarehouse.productWarehouseId()==null)
            {
                ProductWarehouse newProductWarehouse = createProductWarehouseAssociation(ProductWarehouseCreationDto.builder()
                        .warehouseId(productWarehouse.warehouseId())
                        .amount(productWarehouse.amount())
                        .shelveNumber(productWarehouse.shelveNumber())
                        .tierNumber(productWarehouse.tierNumber())
                        .build(), product);
                newProductWarehouses.add(newProductWarehouse);
            }
        }
      //  List<UUID> productwarehousesIds = productDto.getProductWarehouses().stream().map(ProductWarehouseDto::productWarehouseId).collect(Collectors.toList());
     //   List<ProductWarehouse> toSetProductWarehouses = productWarehouseRepository.findAllById(productwarehousesIds);
//       if(productDto.getProductWarehouses().size() != toSetProductWarehouses.size()){
//           for(UUID id: productwarehousesIds){
//               if(!toSetProductWarehouses.contains(id)){
//                   toSetProductWarehouses.add()
//               }
//           }
//       }
//
//        Set<ProductWarehouse> actualProductWarehouses = product.getProductWarehouses();
//        for(ProductWarehouse pw:  toSetProductWarehouses){
//            if(!actualProductWarehouses.contains(pw)){
//
//                productWarehouseRepository.save()
//            }
//        }
        if(volumeDiffer(product, productDto)){
                }
        product.setDimensions(DimensionsMapper.toDimensions(productDto.getDimensionsDto()));
        product.setAmount(productDto.getAmount());
        product.setGroup(warehouseGroupService.getGroup(productDto.getGroup().getId()).orElse(null));

        productRepository.save(product);
        return productDto;
    }

    private boolean volumeDiffer(Product product, ProductDto productDto){
        DimensionsDto dimensionsDto= productDto.getDimensionsDto();
        double productVolume= product.getDimensions().getVolume() * product.getAmount();
        double dtoVolume = dimensionsDto.height()*dimensionsDto.length()*dimensionsDto.width() * productDto.getAmount();
        return productVolume != dtoVolume;
    }
}

