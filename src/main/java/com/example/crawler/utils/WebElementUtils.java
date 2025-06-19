package com.example.crawler.utils;

import com.example.crawler.data.enums.SelectorType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WebElementUtils {

    private static final Integer DEFAULT_TIMEOUT = 15;

    public static WebElement getWebElementVisibility(WebDriver driver, By locator, Integer timeOutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOutSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement getWebElementVisibility(WebDriver driver, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static By getBy(String locatorType, String locatorValue) throws IllegalArgumentException {
        SelectorType type = SelectorType.from(locatorType);
        return switch (type) {
            case ID -> By.id(locatorValue);
            case NAME -> By.name(locatorValue);
            case XPATH -> By.xpath(locatorValue);
            case CSS -> By.cssSelector(locatorValue);
            case TAG -> By.tagName(locatorValue);
            case CLASS -> By.className(locatorValue);
        };
    }

}
