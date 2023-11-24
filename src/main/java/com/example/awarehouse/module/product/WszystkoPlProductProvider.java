package com.example.awarehouse.module.product;

import com.example.awarehouse.module.product.util.CurrencyProvider;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Currency;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@AllArgsConstructor
public class WszystkoPlProductProvider implements ProductProvider{
    private WebDriver driver;
    private final String wszystkoPlUrl = "https://wszystko.pl";
    @Override
    public void getProductsFromSite(String link){
        try {
            driver.get(link);
//            By elementLocator = By.cssSelector("cds-button-icon.cds-button-small.cds-pager__page-button.cds-ripple.cds-pager__number.cds-ripple--bounded.ng-star-inserted");
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            WebElement listOfPages = wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));

            Document document = Jsoup.parse(driver.getPageSource());
            Element listOfPages = document.getElementsByClass("cds-button-icon cds-button-small cds-pager__page-button cds-ripple cds-pager__number cds-ripple--bounded ng-star-inserted").last();
            processProducts(document);
            int lastPage = Integer.parseInt(listOfPages.text());
            for (int i = 2; i <= lastPage; i++) {
                String linkToPage = link + "?page=" + i;
                System.out.println(linkToPage);
                driver.navigate().to(linkToPage);
                Document document1 = Jsoup.parse(driver.getPageSource());
                processProducts(document1);
            }
        }finally {
            driver.quit();
        }
    }

    public void processProducts(Document document){
        Elements productsContainers = document.select(".wpl-offer");
        for (Element productContainer : productsContainers) {
            getProductFromSite(productContainer);
        }
    }

    public void getProductFromSite(Element productContainer){
        Element linkTitle = productContainer.getElementsByClass("cds-text-2 wpl-offer__title wpl-break-word").get(0);
        String title= linkTitle.text();
        Price price = createPoductPrice(productContainer);
        Product product = Product.builder().title(title).price(price).build();
        String linkToProduct = linkTitle.attr("href");
        setInformationAboutProductFromLink(product, linkToProduct);
        System.out.println(product.getTitle()+" "+product.getAmount()+" "+product.getPrice().getAmount()+" "+product.getPrice().getCurrency());

    }
    private Price createPoductPrice(Element productContainer){
        Element priceContainer = productContainer.getElementsByClass("cds-text-2 cds-text-2_bold wpl-offer__price").get(0);
        String price= priceContainer.text();
        BigDecimal decimalPrice = BigDecimal.valueOf(extractDecimalNumberFromString(price));
        Currency currency = extractCurrency(price);
        return new Price(decimalPrice, currency);
    }
    private Double extractDecimalNumberFromString(String textWithNumber){
        Pattern pattern = Pattern.compile("\\d+[.,]?\\d*");
        Matcher matcher = pattern.matcher(textWithNumber);
        if(matcher.find()){
            return extractDecimalNumberFromString(matcher);
        }
        else{
            return 0.0;
        }
    }
    private Double extractDecimalNumberFromString(Matcher matcher){
        String decimalPrice = matcher.group();
        decimalPrice = removeUnsupportedSignsFromStringNumber(decimalPrice);
        return convertToDouble(decimalPrice);
    }
    private String removeUnsupportedSignsFromStringNumber(String number){
        if(number.contains(","))
            number =number.replaceAll(",",".");
        return number;
    }
    private Double convertToDouble(String number){
        try{
            return Double.valueOf(number);
        }catch (NumberFormatException ex)
        {
            return 0.0;
        }

    }

    private Currency extractCurrency(String price){
        Pattern currencyPattern = Pattern.compile("\\p{L}+", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = currencyPattern.matcher(price);
        if(matcher.find()){
            return  extractCurrency(matcher);
        }
        return null;
    }

    private  Currency extractCurrency(Matcher matcher){
        String currencyString = matcher.group();
        return CurrencyProvider.currencyNameMap.get(currencyString);
    }
    void  setInformationAboutProductFromLink(Product product, String linkToProduct){
        driver.navigate().to(wszystkoPlUrl+linkToProduct);
        Document document = Jsoup.parse(driver.getPageSource());
        product.setAmount(getNumberOfProducts(document));
    }
    private Double getNumberOfProducts(Document document){
        Elements numberOfProductsElements = document.getElementsByClass("cds-text-2");
        Optional<Element> optionalNumberOfProducts =numberOfProductsElements.stream().filter(n->n.text().contains("sztuk")).findFirst();
        return optionalNumberOfProducts.map(element -> extractDecimalNumberFromString(element.text())).orElse(null);
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
}
