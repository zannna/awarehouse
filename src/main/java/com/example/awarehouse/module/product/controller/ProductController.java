package com.example.awarehouse.module.product.controller;

import com.example.awarehouse.module.product.ProductProviderService;
import com.example.awarehouse.module.product.ProductService;
import com.example.awarehouse.module.product.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.example.awarehouse.util.Constants.*;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

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
    public ProductDto createProduct(
           @RequestParam(value= "file",  required = false) MultipartFile file,
           @RequestParam String product
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductCreationDto productCreationDto = objectMapper.readValue(product, ProductCreationDto.class);
        return productService.createProduct(file,  productCreationDto);
    }

//    @GetMapping(URI_WAREHOUSE+"/{warehouseId}")
//    public ResponseEntity<Page<ProductDto>> getProductsFromWarehouse(@PathVariable UUID warehouseId, @PageableDefault Pageable pageable){
//        Page<ProductDto> products = productService.getProductsFromWarehouse(warehouseId, pageable);
//        return  ResponseEntity.status(HttpStatus.OK).body(products);
//    }

    @PostMapping(URI_SEARCH)
    public ResponseEntity<Page<ProductDto>> getProductsFromWarehouses( @RequestBody FilterDto filterDto,
                                                                       @PageableDefault Pageable pageable) {
        Page<ProductDto> products = productService.getProductsFromWarehouses(filterDto, pageable);
        return  ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PatchMapping( URI_MOVE )
    public  ResponseEntity<HttpStatus> moveProduct(@RequestBody MoveProductsDto moveProductsDto) {
        productService.moveProducts(moveProductsDto);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(path = "/", method = PATCH, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ProductDto> updateProduct(@RequestPart("product") ProductDto product, @RequestPart(name = "file", required = false) MultipartFile file) {
        ProductDto productResponse = productService.updateProduct(product, file);
        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteProducts(@RequestBody DeleteProductsDto deleteProductsDto) {
        productService.deleteProducts(deleteProductsDto);
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(URI_WAREHOUSE+"/{warehouseId}")
    public ResponseEntity<Page<RowWithProducts>> getProductsByTier(@PathVariable UUID warehouseId, @PageableDefault Pageable pageable){
        Page<RowWithProducts> products = productService.getProductByTier(warehouseId, pageable);
        return  ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping(URI_SEARCH+URI_GROUP)
    public ResponseEntity<Page<ProductDto>> getProductsByGroup( @RequestBody FilterDto filterDto, @PageableDefault Pageable pageable){
        Page<ProductDto> products = productService.getProductsByGroup(filterDto, pageable);
        return  ResponseEntity.status(HttpStatus.OK).body(products);
    }


}
