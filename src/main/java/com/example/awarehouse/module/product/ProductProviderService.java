package com.example.awarehouse.module.product;

import com.example.awarehouse.module.group.WarehouseGroupRepository;
import com.example.awarehouse.module.product.dto.LinkDto;
import com.example.awarehouse.module.product.dto.ProductDTO;
import com.example.awarehouse.module.product.util.exception.ProductProviderNotExistException;
import com.example.awarehouse.module.warehouse.WarehouseService;
import lombok.AllArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import static com.example.awarehouse.module.product.util.ProductConstants.PRODUCT_NOT_EXIST;

@Service
@AllArgsConstructor
public class ProductProviderService {
    private ProductRepository productRepository;
    private WarehouseGroupRepository warehouseGroupRepository;
    private WarehouseService warehouseService;


    public PageImpl<ProductDTO> createProductsForWarehouseFromSite(String provider, LinkDto linkDto) throws IOException {
        WebDriver webDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), new ChromeOptions());
        ProductProvider productProvider = getProductProviderForWarehouse(webDriver, provider, linkDto.associateElementId());
        return productProvider.getProductsFromSite(linkDto.link());
    }

    public PageImpl<ProductDTO> createProductsForGroupFromSite(String provider, LinkDto linkDto) throws IOException {
        WebDriver webDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), new ChromeOptions());
        ProductProvider productProvider = getProductProviderForGroup(webDriver, provider, linkDto.associateElementId());
        return productProvider.getProductsFromSite(linkDto.link());
    }

    private ProductProvider getProductProviderForWarehouse(WebDriver webDriver, String provider, UUID warehouseId) {
        if (provider.equals("WSZYSTKO_PL")) {
            return new WszystkoPlProductProviderForWarehouse(webDriver, warehouseId, productRepository, warehouseService);
        } else {
            throw new ProductProviderNotExistException(PRODUCT_NOT_EXIST);
        }
    }

    private ProductProvider getProductProviderForGroup(WebDriver webDriver, String provider, UUID groupId) {
        if (provider.equals("WSZYSTKO_PL")) {
            return new WszystkoPlProductProviderForGroup(webDriver, groupId, warehouseGroupRepository, productRepository);
        } else {
            throw new ProductProviderNotExistException(PRODUCT_NOT_EXIST);
        }
    }
}
