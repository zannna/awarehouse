package com.example.awarehouse.util.configuration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
@Component
public class WebDriverProvider {


    @Value("${selenium.grid.url}")
    private static String seleniumGridUrl;

    public static WebDriver webDriver() throws MalformedURLException {
        return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), new ChromeOptions());
    }
}