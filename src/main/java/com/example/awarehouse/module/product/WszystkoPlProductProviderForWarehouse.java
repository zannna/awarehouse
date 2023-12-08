package com.example.awarehouse.module.product;

import com.example.awarehouse.module.product.dto.ProductDTO;
import com.example.awarehouse.module.product.mapper.ProductMapper;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WarehouseRepository;
import com.example.awarehouse.module.warehouse.WarehouseService;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class WszystkoPlProductProviderForWarehouse extends WszystkoPlProductProvider{
    ProductRepository productRepository;
  WarehouseService warehouseService;
    Warehouse warehouse;

    public WszystkoPlProductProviderForWarehouse(WebDriver driver, UUID associateElementId,  ProductRepository productRepository, WarehouseService warehouseService) {
        super(driver, associateElementId);
        this.productRepository = productRepository;
        this.warehouseService = warehouseService;
         warehouse= warehouseService.getWarehouse(associateElementId).orElse(null);
    }

    @Override
    void saveProduct(Product product) {
        product= productRepository.save(product);
        ProductWarehouse productWarehouse = new ProductWarehouse(product, warehouse, product.getAmount());
        product.setProductWarehouses(Set.of(productWarehouse));
        ProductDTO productDTO = ProductMapper.withWarehouseToDto(product, productWarehouse);
        addedProducts.add(productDTO);
    }
}
