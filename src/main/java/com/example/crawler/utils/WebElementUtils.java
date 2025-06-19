package com.example.crawler.utils;

import com.example.crawler.data.enums.SelectorTypeEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WebElementUtils {

    private static final Integer DEFAULT_TIMEOUT = 15;

    private static final Integer DEFAULT_RETRY = 3;

    private static final Integer DEFAULT_RETRY_DELAY = 5;

    public static WebElement getWebElementVisibility(WebDriver driver, By locator, Integer timeOutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOutSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement getWebElementVisibility(WebDriver driver, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @SneakyThrows
    public static WebElement getWebElementWithRetry(WebDriver driver, By locator) {
        return getWebElementWithRetry(driver, locator, DEFAULT_TIMEOUT, DEFAULT_RETRY, DEFAULT_RETRY_DELAY);
    }

    @SneakyThrows
    public static WebElement getWebElementWithRetry(WebDriver driver, By locator, Integer timeOuts) {
        return getWebElementWithRetry(driver, locator, timeOuts, DEFAULT_RETRY, DEFAULT_RETRY_DELAY);
    }


    @SneakyThrows
    public static WebElement getWebElementWithRetry(WebDriver driver, By locator, Integer timeOutSeconds, Integer maxRetries, Integer retryDelay) {
        int attempts = 0;
        while (attempts < maxRetries) {
            try {
                return getWebElementVisibility(driver, locator, timeOutSeconds);
            } catch (Exception e) {
                attempts++;
                log.error("fail to getWebElement attempt no.{} ", attempts);
                Thread.sleep(Duration.ofSeconds(retryDelay));
            }
        }
        throw new TimeoutException("Timed out waiting for " + maxRetries + " retries");
    }

    public static By getBy(String locatorType, String locatorValue) throws IllegalArgumentException {
        SelectorTypeEnum type = SelectorTypeEnum.from(locatorType);
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
