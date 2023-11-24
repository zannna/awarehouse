package com.example.awarehouse.module.product.controller;

import com.example.awarehouse.module.product.ProductService;
import com.example.awarehouse.module.product.dto.LinkDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;

import static com.example.awarehouse.util.Constants.*;

@RestController
@RequestMapping(URI_VERSION_V1+URI_PRODUCT)
@AllArgsConstructor
public class ProductController {

    ProductService productService;

    @PostMapping(URI_PROVIDER)
    public void createProductsFromSite(@RequestParam String warehouseName, @RequestParam String site,
                                      @RequestBody LinkDto urlProviderDto) throws IOException {
        productService.createProductsFromSite(warehouseName, site, urlProviderDto);
    }

    @GetMapping
    public void getProducts() throws MalformedURLException {
        productService.fun();
    }
}
