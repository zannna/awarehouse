package com.example.awarehouse.module.product;

import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.group.WarehouseGroupRepository;
import com.example.awarehouse.module.product.dto.ProductDto;
import com.example.awarehouse.module.product.mapper.ProductMapper;
import org.openqa.selenium.WebDriver;

import java.util.UUID;

public class WszystkoPlProductProviderForGroup extends WszystkoPlProductProvider{

    private WarehouseGroupRepository warehouseGroupRepository;
    private ProductRepository productRepository;

    public WszystkoPlProductProviderForGroup(WebDriver driver, UUID associateElementId, WarehouseGroupRepository warehouseGroupRepository, ProductRepository productRepository) {
        super(driver, associateElementId);
        this.warehouseGroupRepository = warehouseGroupRepository;
        this.productRepository = productRepository;
    }

    @Override
    void saveProduct(Product product) {
       WarehouseGroup group = warehouseGroupRepository.findById(associateElementId).get();
       product.setGroup(group);
       product= productRepository.save(product);
        ProductDto productDTO = ProductMapper.toDto(product, null);
       addedProducts.add(productDTO);
    }
}
