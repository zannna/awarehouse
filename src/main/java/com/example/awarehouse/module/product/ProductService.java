package com.example.awarehouse.module.product;

import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.group.WarehouseGroupService;
import com.example.awarehouse.module.product.dto.PriceDto;
import com.example.awarehouse.module.product.dto.ProductCreationDto;
import com.example.awarehouse.module.product.dto.ProductDTO;
import com.example.awarehouse.module.product.mapper.ProductMapper;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WarehouseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService {
private ProductRepository productRepository;
private WarehouseService warehouseService;
private WarehouseGroupService warehouseGroupService;

    public List<ProductDTO> createProduct(ProductCreationDto productDto) {
        checkIfWarehouseIdAndGroupIdNotNull(productDto);
        UUID groupId = productDto.getGroupId();
        WarehouseGroup group = getGroup(groupId, productDto.getAmountGroup());
        Price price = getPrice(productDto);
        Product product = new Product(productDto.getTitle(), productDto.getAmountGroup(), price, productDto.getPhoto(), group);
        Product savedProduct = productRepository.save(product);
        UUID warehouseId = productDto.getWarehouseId();
        List<ProductDTO> products= new ArrayList<>();
        if(groupId != null && productDto.getAmountGroup() != productDto.getAmountWarehouse()){
           products.add(ProductMapper.withGroupToDto(savedProduct));
        }
        if (warehouseId != null) {
            ProductWarehouse productWarehouse = createProductWarehouseAssociation(savedProduct, warehouseId, productDto.getAmountWarehouse());
            products.add(ProductMapper.withWarehouseToDto(product,productWarehouse));
        }
        return products;
    }
    private void checkIfWarehouseIdAndGroupIdNotNull(ProductCreationDto productDto){
        UUID warehouseId = productDto.getWarehouseId();
        UUID groupId = productDto.getGroupId();
        if(warehouseId ==null && groupId== null){
            throw new IllegalArgumentException("WarehouseId or groupId must be provided");
        }
        else if(warehouseId !=null && groupId!= null && productDto.getAmountGroup() ==0 && productDto.getAmountWarehouse() ==0){
            throw new IllegalArgumentException("WarehouseId and groupId cannot be provided at the same time");
        }
    }

    private ProductWarehouse createProductWarehouseAssociation(Product product, UUID warehouseId, Double amountWarehouse) {
        if(warehouseIsSet(warehouseId, amountWarehouse)) {
            Warehouse warehouse = warehouseService.getWarehouse(warehouseId).orElse(null);
            ProductWarehouse productWarehouse = new ProductWarehouse(product, warehouse, amountWarehouse);
           return productWarehouse;
        }
        return null;
    }

    private boolean warehouseIsSet(UUID warehouseId, Double amountWarehouse){
        if(warehouseId == null){
            return false;
        }
        if (amountWarehouse == null) {
            throw new IllegalArgumentException("AmountWarehouse must be provided");
        }
        return true;
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

}



