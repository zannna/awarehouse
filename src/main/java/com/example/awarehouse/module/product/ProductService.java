package com.example.awarehouse.module.product;

import com.example.awarehouse.module.product.dto.LinkDto;
import com.example.awarehouse.module.product.util.exception.ProductProviderNotExistException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.awarehouse.module.product.util.ProductConstants.PRODUCT_NOT_EXIST;

@Service
@AllArgsConstructor
public class ProductService {
    ProductRepository productRepository;

    public void createProductsFromSite(String warehouseName, String provider, LinkDto linkDto) {
        ProductProvider productProvider = getProductProvider(provider);
        productProvider.getProductsFromSite(linkDto.link());
    }

    private ProductProvider getProductProvider(String provider){
        if(provider.equals("ALLEGRO")){
            return new AllegroProductProvider();
        }
        else{
            throw new ProductProviderNotExistException(PRODUCT_NOT_EXIST);
        }
    }
}



