package pages;

import base.DriverSetup;
import locators.LocatorRepository;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitUtils;

import java.time.Duration;

public class CityPage {
    private final WebDriver driver;

    public CityPage() {
        this.driver = DriverSetup.getDriver();
        System.out.println("✅ CityPage initialized with WebDriver instance.");
    }

    // ✅ Utility: safe click (handles overlay + fallback to JS)
    private void safeClick(WebElement element) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (Exception e) {
            System.out.println("⚠️ Normal click failed, retrying with JS click...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    // ✅ Utility: close popup overlay if present
    private void closePopupIfPresent() {
        try {
            WebElement popup = driver.findElement(LocatorRepository.get("popupClose"));
            if (popup.isDisplayed()) {
                popup.click();
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.invisibilityOf(popup));
                System.out.println("↩️ Closed blocking popup overlay.");
            }
        } catch (Exception ignored) {}
    }

    // ✅ Select city (works with suggestion or popular city)
    public boolean selectCity(String cityName) {
        try {
            System.out.println("🌆 Attempting to select city: " + cityName);
            closePopupIfPresent();

            WebElement currentCity = driver.findElement(By.xpath("//span[contains(@class,'sc-1or3vea-16')]"));
            safeClick(currentCity);

            WebElement cityInput = WaitUtils.visible(LocatorRepository.get("cityInput"), 10);
            cityInput.clear();
            cityInput.sendKeys(cityName);

            WebElement suggestion = WaitUtils.clickable(By.xpath("//p[contains(text(),'" + cityName + "')]"), 10);
            safeClick(suggestion);

            System.out.println("✅ City selection successful: " + cityName);
            return true;
        } catch (Exception e) {
            System.out.println("❌ Failed to select city: " + e.getMessage());
            return false;
        }
    }

    // ✅ Change city
    public boolean changeLocation(String cityName) {
        try {
            System.out.println("🌍 Changing city to: " + cityName);
            closePopupIfPresent();

            WebElement currentCity = WaitUtils.clickable(By.xpath("//span[contains(@class,'sc-1or3vea-16')]"), 10);
            safeClick(currentCity);

            WebElement cityInput = WaitUtils.visible(LocatorRepository.get("cityInput"), 10);
            cityInput.clear();
            cityInput.sendKeys(cityName);

            WebElement suggestion = WaitUtils.clickable(By.xpath("//p[contains(text(),'" + cityName + "')]"), 10);
            safeClick(suggestion);

            System.out.println("✅ Changed city to: " + cityName);
            return true;
        } catch (Exception e) {
            System.out.println("❌ Failed to change city: " + e.getMessage());
            return false;
        }
    }

    // ✅ Select city by Popular City icon (works with Delhi-NCR too)
    public boolean selectCityByIcon(String cityName) {
        try {
            System.out.println("🌆 Selecting city by icon: " + cityName);
            closePopupIfPresent();

            WebElement currentCity = WaitUtils.clickable(By.xpath("//span[contains(@class,'sc-1or3vea-16')]"), 10);
            safeClick(currentCity);

            // Flexible locator (handles Delhi / Delhi-NCR)
            WebElement icon = WaitUtils.clickable(By.xpath("//p[contains(text(),'" + cityName + "')]"), 10);
            safeClick(icon);

            System.out.println("✅ City icon selected: " + cityName);
            return true;
        } catch (Exception e) {
            System.out.println("❌ City icon not found: " + cityName + " → " + e.getMessage());
            return false;
        }
    }

    // ✅ Toggle "View All Cities"
    public boolean viewAllCitiesToggle() {
        try {
            System.out.println("🌍 Attempting to toggle 'View All Cities'...");
            closePopupIfPresent();

            WebElement currentCity = WaitUtils.clickable(By.xpath("//span[contains(@class,'sc-1or3vea-16')]"), 10);
            safeClick(currentCity);

            // If already expanded, skip clicking
            boolean alreadyExpanded = !driver.findElements(LocatorRepository.get("cityList")).isEmpty();
            if (alreadyExpanded) {
                System.out.println("ℹ️ 'View All Cities' already expanded.");
                return true;
            }

            // Else expand
            WebElement toggle = WaitUtils.clickable(LocatorRepository.get("allCities"), 10);
            safeClick(toggle);

            WaitUtils.visible(LocatorRepository.get("cityList"), 10);
            System.out.println("✅ 'View All Cities' expanded successfully.");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Failed to toggle 'View All Cities': " + e.getMessage());
            return false;
        }
    }
}
