package com.example.awarehouse.module.product;

import com.example.awarehouse.module.product.dto.LinkDto;
import com.example.awarehouse.module.product.util.exception.ProductProviderNotExistException;
import com.example.awarehouse.util.configuration.WebDriverProvider;
import lombok.AllArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;

import static com.example.awarehouse.module.product.util.ProductConstants.PRODUCT_NOT_EXIST;

@Service
@AllArgsConstructor
public class ProductService {
    ProductRepository productRepository;

    public void createProductsFromSite(String warehouseName, String provider, LinkDto linkDto) throws IOException {
        ProductProvider productProvider = getProductProvider(provider);
        productProvider.getProductsFromSite(linkDto.link());
    }

    private ProductProvider getProductProvider(String provider){
        if(provider.equals("ALLEGRO")){
            WebDriver webDriver=null;
            return new WszystkoPlProductProvider(webDriver);
        }
        else{
            throw new ProductProviderNotExistException(PRODUCT_NOT_EXIST);
        }
    }

    public void fun() throws MalformedURLException {
        WebDriver webDriver= WebDriverProvider.webDriver();
        ProductProvider productProvider = new WszystkoPlProductProvider(webDriver);
        productProvider.getProductsFromSite("https://wszystko.pl/sprzedawca/drogerianikolapl");
    }
}



