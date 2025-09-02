package pages;

import base.DriverSetup;
import locators.LocatorRepository;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitUtils;

import java.time.Duration;

public class CityPage {
    private final WebDriver webDriver;

    public CityPage() {
        this.webDriver = DriverSetup.getDriver();
        System.out.println("CityPage initialized with WebDriver.");
    }

    private void performClick(WebElement element) {
        try {
            new WebDriverWait(webDriver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (Exception e) {
            System.out.println("Normal click failed, trying JS click...");
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element);
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", element);
        }
    }

    private void dismissPopup() {
        try {
            WebElement popupElement = webDriver.findElement(LocatorRepository.get("popupClose"));
            if (popupElement.isDisplayed()) {
                popupElement.click();
                new WebDriverWait(webDriver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.invisibilityOf(popupElement));
                System.out.println("Closed popup overlay.");
            }
        } catch (Exception ignored) {}
    }

    public boolean chooseCity(String city) {
        try {
            System.out.println("Attempting to select city: " + city);
            dismissPopup();

            WebElement currentCityElement = webDriver.findElement(By.xpath("//span[contains(@class,'sc-1or3vea-16')]"));
            performClick(currentCityElement);

            WebElement inputBox = WaitUtils.visible(LocatorRepository.get("cityInput"), 10);
            inputBox.clear();
            inputBox.sendKeys(city);

            WebElement suggestionElement = WaitUtils.clickable(By.xpath("//p[contains(text(),'" + city + "')]"), 10);
            performClick(suggestionElement);

            System.out.println("City selected: " + city);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to select city: " + e.getMessage());
            return false;
        }
    }

    public boolean updateLocation(String city) {
        try {
            System.out.println("Changing city to: " + city);
            dismissPopup();

            WebElement currentCityElement = WaitUtils.clickable(By.xpath("//span[contains(@class,'sc-1or3vea-16')]"), 10);
            performClick(currentCityElement);

            WebElement inputBox = WaitUtils.visible(LocatorRepository.get("cityInput"), 10);
            inputBox.clear();
            inputBox.sendKeys(city);

            WebElement suggestionElement = WaitUtils.clickable(By.xpath("//p[contains(text(),'" + city + "')]"), 10);
            performClick(suggestionElement);

            System.out.println("City changed to: " + city);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to change city: " + e.getMessage());
            return false;
        }
    }

    public boolean chooseCityByIcon(String city) {
        try {
            System.out.println("Selecting city by icon: " + city);
            dismissPopup();

            WebElement currentCityElement = WaitUtils.clickable(By.xpath("//span[contains(@class,'sc-1or3vea-16')]"), 10);
            performClick(currentCityElement);

            WebElement iconElement = WaitUtils.clickable(By.xpath("//p[contains(text(),'" + city + "')]"), 10);
            performClick(iconElement);

            System.out.println("City icon selected: " + city);
            return true;
        } catch (Exception e) {
            System.out.println("City icon not found: " + city + " → " + e.getMessage());
            return false;
        }
    }

    public boolean expandAllCities() {
        try {
            System.out.println("Attempting to expand 'View All Cities'...");
            dismissPopup();

            WebElement currentCityElement = WaitUtils.clickable(By.xpath("//span[contains(@class,'sc-1or3vea-16')]"), 10);
            performClick(currentCityElement);

            boolean alreadyExpanded = !webDriver.findElements(LocatorRepository.get("cityList")).isEmpty();
            if (alreadyExpanded) {
                System.out.println("'View All Cities' already expanded.");
                return true;
            }

            WebElement toggleElement = WaitUtils.clickable(LocatorRepository.get("allCities"), 10);
            performClick(toggleElement);

            WaitUtils.visible(LocatorRepository.get("cityList"), 10);
            System.out.println("'View All Cities' expanded successfully.");
            return true;
        } catch (Exception e) {
            System.out.println("Failed to expand 'View All Cities': " + e.getMessage());
            return false;
        }
    }
}
