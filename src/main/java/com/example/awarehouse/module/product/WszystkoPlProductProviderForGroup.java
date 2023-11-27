package com.example.awarehouse.module.product;

import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.group.WarehouseGroupRepository;
import com.example.awarehouse.module.product.dto.ProductDTO;
import com.example.awarehouse.module.product.mapper.ProductMapper;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.GroupNotExistException;
import lombok.AllArgsConstructor;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.UUID;

import static com.example.awarehouse.module.warehouse.util.WarehouseConstants.GROUP_NOT_EXIST;

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
        ProductDTO productDTO = ProductMapper.mapToDto(product);
       addedProducts.add(productDTO);
    }
}
