package com.example.awarehouse.module.product;

import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.group.WarehouseGroupService;
import com.example.awarehouse.module.product.dto.*;
import com.example.awarehouse.module.product.mapper.ProductMapper;
import com.example.awarehouse.module.product.mapper.ProductWarehouseMapper;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WarehouseService;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTierService;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WarehouseNotExistException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.awarehouse.module.warehouse.util.WarehouseConstants.WAREHOUSE_NOT_EXIST;

@Service
@AllArgsConstructor
public class ProductService {
private ProductRepository productRepository;
private WarehouseService warehouseService;
private WarehouseGroupService warehouseGroupService;
private ShelveTierService tierService;
private ProductWarehouseRepository productWarehouseRepository;

    @Transactional
    public ProductDto createProduct(ProductCreationDto productDto) {
        Product savedProduct = product(productDto);
        List<ProductWarehouseDto> productWarehouses = createProductWarehouseAssociations(savedProduct, productDto.getProductWarehouses());
        setGroupAmountIfNotExist(savedProduct, productWarehouses);
        return ProductMapper.toDto(savedProduct, productWarehouses);
    }
private Product product(ProductCreationDto productDto) {
    checkIfWarehouseIdAndGroupIdNotNull(productDto);
    UUID groupId = productDto.getGroupId();
    WarehouseGroup group = getGroup(groupId, productDto.getAmountGroup());
    Price price = getPrice(productDto);
    Product product = new Product(productDto.getTitle(), productDto.getAmountGroup(), price, productDto.getPhoto(), group);
    Product savedProduct = productRepository.save(product);
    return savedProduct;
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

    private Price getPrice(ProductCreationDto productDto){
        PriceDto priceDto = productDto.getPrice();
        return new Price(priceDto.getAmount(), priceDto.getCurrency());
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
        Optional<ShelveTier> tier = Optional.empty();
        if(shelveProvided(productWarehouseCreationDto)) {
            tier = Optional.of(tierService.getShelveTier(warehouseId, productWarehouseCreationDto.shelveNumber(), productWarehouseCreationDto.tierNumber()));
        }
        ProductWarehouse productWarehouse = new ProductWarehouse(product, warehouse,  productWarehouseCreationDto.amount(), tier);
        productWarehouseRepository.save(productWarehouse);
        return productWarehouse;
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

}



