package com.example.awarehouse.module.product;

import com.example.awarehouse.module.product.dto.ProductDTO;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface ProductProvider {
    PageImpl<ProductDTO> getProductsFromSite(String link);
}
