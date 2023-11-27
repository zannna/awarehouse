package com.example.awarehouse.module.product;

import com.example.awarehouse.module.product.dto.ProductDTO;
import com.example.awarehouse.module.product.mapper.ProductMapper;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.UUID;

public class WszystkoPlProductProviderForWarehouse extends WszystkoPlProductProvider{
    ProductRepository productRepository;
    ProductWarehouseRepository productWarehouseRepository;

    public WszystkoPlProductProviderForWarehouse(WebDriver driver, UUID associateElementId,  ProductRepository productRepository, ProductWarehouseRepository productWarehouseRepository) {
        super(driver, associateElementId);
        this.productRepository = productRepository;
        this.productWarehouseRepository = productWarehouseRepository;
    }

    @Override
    void saveProduct(Product product) {
        product= productRepository.save(product);
        productWarehouseRepository.createProductWarehouseAssociation(product.getId(), associateElementId, product.getAmount());
        ProductDTO productDTO = ProductMapper.mapToDto(product);
        addedProducts.add(productDTO);
    }
}
