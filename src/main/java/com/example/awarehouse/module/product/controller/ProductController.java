package com.example.awarehouse.module.product.controller;

import com.example.awarehouse.module.product.ProductProviderService;
import com.example.awarehouse.module.product.ProductService;
import com.example.awarehouse.module.product.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.awarehouse.util.Constants.*;

@RestController
@RequestMapping(URI_VERSION_V1+URI_PRODUCT)
@AllArgsConstructor
public class ProductController {

    ProductProviderService productProviderService;
    ProductService productService;

    @PostMapping(URI_WAREHOUSE)
    public PageImpl<ProductDto> createProductsForWarehouseFromSite(@RequestParam String provider,
                                                                   @RequestBody LinkDto urlProviderDto) throws IOException {
        return productProviderService.createProductsForWarehouseFromSite(provider, urlProviderDto);
    }
    @PostMapping(URI_GROUP)
    public PageImpl<ProductDto> createProductsForGroupFromSite(@RequestParam String provider,
                                                               @RequestBody LinkDto urlProviderDto) throws IOException {
        return productProviderService.createProductsForGroupFromSite(provider, urlProviderDto);
    }
    @PostMapping
    public ProductDto createProduct(@RequestBody ProductCreationDto productDto) {
        return productService.createProduct(productDto);
    }

    @GetMapping(URI_WAREHOUSE+"/{warehouseId}")
    public ResponseEntity<Page<ProductDto>> getProductsFromWarehouse(@PathVariable UUID warehouseId, @PageableDefault Pageable pageable) {
        Page<ProductDto> products = productService.getProductsFromWarehouse(warehouseId, pageable);
        return  ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping(URI_WAREHOUSE)
    public ResponseEntity<List<ProductDto>> getProductsFromWarehouses(@RequestBody List<UUID> warehouseIds, @PageableDefault Pageable pageable) {
        List<ProductDto> products = productService.getProductsFromWarehouses(warehouseIds, pageable);
        return  ResponseEntity.status(HttpStatus.OK).body(products);
    }

}
