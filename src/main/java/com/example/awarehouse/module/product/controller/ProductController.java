package com.example.awarehouse.module.product.controller;

import com.example.awarehouse.module.product.ProductService;
import com.example.awarehouse.module.product.dto.LinkDto;
import com.example.awarehouse.module.product.dto.ProductDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;

import static com.example.awarehouse.util.Constants.*;

@RestController
@RequestMapping(URI_VERSION_V1+URI_PRODUCT)
@AllArgsConstructor
public class ProductController {

    ProductService productService;

    @PostMapping(URI_WAREHOUSE)
    public PageImpl<ProductDTO> createProductsForWarehouseFromSite(@RequestParam String provider,
                                                                   @RequestBody LinkDto urlProviderDto) throws IOException {
        return productService.createProductsForWarehouseFromSite(provider, urlProviderDto);
    }
    @PostMapping(URI_GROUP)
    public PageImpl<ProductDTO> createProductsForGroupFromSite(@RequestParam String provider,
                                                               @RequestBody LinkDto urlProviderDto) throws IOException {
        return productService.createProductsForGroupFromSite(provider, urlProviderDto);
    }

    @GetMapping
    public void getProducts() throws MalformedURLException {
        productService.fun();
    }
}
