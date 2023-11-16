package com.example.awarehouse.module.product;

import com.example.awarehouse.module.product.util.exception.SiteIsNotAvailableException;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static com.example.awarehouse.module.product.util.ProductConstants.SITE_NOT_AVAILABLE;

public class AllegroProductProvider implements ProductProvider{
    @Override
    public void getProductsFromSite(String link) {
        Document document = getDocumentFromSite(link);
        Elements productsMainContainer = document.getElementsByClass("mg9e_16 mj7a_16 mg9e_24_l mj7a_24_l");

    }

    public Document getDocumentFromSite(String link){
        try {
            return Jsoup.connect(link).get();
        } catch (HttpStatusException ex) {
            throw new SiteIsNotAvailableException(SITE_NOT_AVAILABLE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
