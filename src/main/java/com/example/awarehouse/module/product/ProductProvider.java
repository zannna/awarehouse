package com.example.awarehouse.module.product;

import com.example.awarehouse.module.product.dto.ProductDto;
import org.springframework.data.domain.PageImpl;

public interface ProductProvider {
    PageImpl<ProductDto> getProductsFromSite(String link);
}
