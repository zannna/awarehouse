package com.example.awarehouse.module.product;

import com.example.awarehouse.module.product.dto.ProductDTO;
import com.example.awarehouse.module.product.util.CurrencyProvider;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@AllArgsConstructor
public abstract class WszystkoPlProductProvider implements ProductProvider {

    protected final WebDriver driver;
    private final String wszystkoPlUrl = "https://wszystko.pl";

    protected UUID associateElementId;

    protected List<ProductDTO> addedProducts = new ArrayList<>();

    public WszystkoPlProductProvider(WebDriver driver, UUID associateElementId) {
        this.driver = driver;
        this.associateElementId = associateElementId;
    }

    @Override
    public PageImpl<ProductDTO> getProductsFromSite(String link) {
        try {
            Document firstDocument = processFirstPage(link);
            int lastPage = getLastPage(firstDocument);
            List<String> unloadedPages = processPages(lastPage, link);
            processProductsFromUnloadedPage(unloadedPages);

        } finally {
            driver.quit();
        }
        return new PageImpl<>(addedProducts);
    }

    private Document processFirstPage(String link) {
        driver.get(link);
        Document document = Jsoup.parse(driver.getPageSource());
        pageLoaded(60, ".wpl-offer", 4);
        processProducts(document);
        return document;
    }

    private int getLastPage(Document document) {
        Element listOfPages = document.getElementsByClass("cds-button-icon cds-button-small cds-pager__page-button " +
                "cds-ripple cds-pager__number cds-ripple--bounded ng-star-inserted").last();
        return Integer.parseInt(listOfPages.text());
    }

    private List<String> processPages(int lastPage, String link) {
        List<String> tryAgainPages = new ArrayList<>();
        for (int i = 2; i <= lastPage; i++) {
            String linkToPage = link + "?page=" + i;
            System.out.println(linkToPage);
            driver.navigate().to(linkToPage);
            if (pageLoaded(60, ".wpl-offer", 3)) {
                Document document1 = Jsoup.parse(driver.getPageSource());
                processProducts(document1);
            } else {
                tryAgainPages.add(linkToPage);
            }
        }
        return tryAgainPages;
    }

    private List<String> processProductsFromUnloadedPage(List<String> tryAgainPages) {
        List<String> unloadedPages = new ArrayList<>();
        for (String tryAgainPage : tryAgainPages) {
            driver.navigate().to(tryAgainPage);
            if (pageLoaded(60, ".wpl-offer", 3)) {
                Document document = Jsoup.parse(driver.getPageSource());
                processProducts(document);
            } else {
                unloadedPages.add(tryAgainPage);
            }
        }
        return unloadedPages;
    }

    private void processProducts(Document document) {
        Elements productsContainers = document.select(".wpl-offer");
        for (Element productContainer : productsContainers) {
            saveProduct(createProduct(productContainer));
        }
    }

    abstract void saveProduct(Product product);

    private Product createProduct(Element productContainer) {
        Element linkTitle = productContainer.getElementsByClass("cds-text-2 wpl-offer__title wpl-break-word").get(0);
        String title = linkTitle.text();
        Price price = createPoductPrice(productContainer);
        Product product = Product.builder().title(title).price(price).build();
        String linkToProduct = linkTitle.attr("href");
        setInformationAboutProductFromLink(product, linkToProduct);
        System.out.println(product.getTitle() + " " + product.getAmount() + " " + product.getPrice().getAmount() + " " + product.getPrice().getCurrency());
        return product;
    }

    private Price createPoductPrice(Element productContainer) {
        Element priceContainer = productContainer.getElementsByClass("cds-text-2 cds-text-2_bold wpl-offer__price").get(0);
        String price = priceContainer.text();
        BigDecimal decimalPrice = BigDecimal.valueOf(extractDecimalNumberFromString(price));
        Currency currency = extractCurrency(price);
        return new Price(decimalPrice, currency);
    }

    private Double extractDecimalNumberFromString(String textWithNumber) {
        Pattern pattern = Pattern.compile("\\d+[.,]?\\d*");
        Matcher matcher = pattern.matcher(textWithNumber);
        if (matcher.find()) {
            return extractDecimalNumberFromString(matcher);
        } else {
            return 0.0;
        }
    }

    private Double extractDecimalNumberFromString(Matcher matcher) {
        String decimalPrice = matcher.group();
        decimalPrice = removeUnsupportedSignsFromStringNumber(decimalPrice);
        return convertToDouble(decimalPrice);
    }

    private String removeUnsupportedSignsFromStringNumber(String number) {
        if (number.contains(",")) number = number.replaceAll(",", ".");
        return number;
    }

    private Double convertToDouble(String number) {
        try {
            return Double.valueOf(number);
        } catch (NumberFormatException ex) {
            return 0.0;
        }

    }

    private Currency extractCurrency(String price) {
        Pattern currencyPattern = Pattern.compile("\\p{L}+", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = currencyPattern.matcher(price);
        if (matcher.find()) {
            return extractCurrency(matcher);
        }
        return null;
    }

    private Currency extractCurrency(Matcher matcher) {
        String currencyString = matcher.group();
        return CurrencyProvider.currencyNameMap.get(currencyString);
    }

    void setInformationAboutProductFromLink(Product product, String linkToProduct) {
        driver.navigate().to(wszystkoPlUrl + linkToProduct);
        if (pageLoaded(60, ".wpl-offer-presentation-summary__quantity-section", 3)) {
            Document document = Jsoup.parse(driver.getPageSource());
            product.setAmount(getNumberOfProducts(document));
        } else {
            product.setAmount(0.0);
        }

    }

    private Double getNumberOfProducts(Document document) {
        Elements numberOfProductsElements = document.getElementsByClass("cds-text-2");
        Optional<Element> optionalNumberOfProducts = numberOfProductsElements.stream().filter(n -> n.text().contains("sztuk")).findFirst();
        return optionalNumberOfProducts.map(element -> extractDecimalNumberFromString(element.text())).orElse(0.0);
    }

    private boolean pageLoaded(int seconds, String cssSelector, int reptitions) {
        while (reptitions > 0) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector)));
                return true;
            } catch (Exception ex) {
                reptitions--;
            }
        }
        return false;
    }

    private void removePopup() {
        WebElement popup = driver.findElement(By.cssSelector("div.wpl-cookie-popup__container"));
        if (popup.isDisplayed()) {
            popup.findElement(By.cssSelector("button.cds-button-secondary")).click();
        }
    }
}
//    public Document getDocumentFromSite(String link){
//       try {
//            System.out.println(link);
//            return  Jsoup
//                .connect(link)
//                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:100.0) Gecko/20100101 Firefox/100.0") //
//                .get();
//        } catch (HttpStatusException ex) {
//            throw new SiteIsNotAvailableException(ex.getMessage());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

