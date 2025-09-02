package utils;

import base.DriverSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    // Always fetch the driver dynamically instead of caching a null reference
    private static WebDriver getDriver() {
        return DriverSetup.getDriver();
    }

    // Wait until element is clickable
    public static WebElement clickable(By locator, int timeoutInSeconds) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutInSeconds))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Wait until element is visible
    public static WebElement visible(By locator, int timeoutInSeconds) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutInSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // ✅ Wait until URL contains given text
    public static boolean urlContains(String text, int timeoutInSeconds) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutInSeconds))
                .until(ExpectedConditions.urlContains(text));
    }
}
