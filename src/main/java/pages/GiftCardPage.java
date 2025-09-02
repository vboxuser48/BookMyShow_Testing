package pages;

import base.DriverSetup;
import locators.LocatorRepository;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

public class GiftCardPage {
    private final WebDriver webDriver;

    public GiftCardPage() {
        this.webDriver = DriverSetup.getDriver();
        System.out.println("GiftCardPage initialized with WebDriver.");
    }

    private void dismissPopup() {
        try {
            WebElement popupElement = webDriver.findElement(LocatorRepository.get("popupClose"));
            if (popupElement.isDisplayed()) {
                System.out.println("Closing popup overlay before clicking Gift Cards...");
                popupElement.click();
                Thread.sleep(1000);
            }
        } catch (Exception ignored) {}
    }

    public boolean openGiftCardsAndVerifyBalance() {
        try {
            System.out.println("Opening Gift Cards section...");
            dismissPopup();
            WaitUtils.clickable(LocatorRepository.get("giftTab"), 10).click();

            WebElement balanceElement = WaitUtils.clickable(LocatorRepository.get("checkBalanceBtn"), 10);
            boolean isVisible = balanceElement.isDisplayed();
            if (isVisible) {
                System.out.println("'Check Gift Card Balance' icon is visible.");
            }
            return isVisible;
        } catch (Exception e) {
            System.out.println("Failed to validate Gift Cards section: " + e.getMessage());
            return false;
        }
    }

    public String enterInvalidVoucherAndFetchError(String voucherCode) {
        try {
            System.out.println("Clicking on 'Check Balance' icon...");
            dismissPopup();
            WaitUtils.clickable(LocatorRepository.get("checkBalanceBtn"), 10).click();

            WebElement inputBox = WaitUtils.visible(LocatorRepository.get("voucherInput"), 10);
            inputBox.clear();
            inputBox.sendKeys(voucherCode);
            System.out.println("Entered voucher: " + voucherCode);

            WebElement errorElement = WaitUtils.visible(LocatorRepository.get("errorGiftVoucher"), 10);
            return errorElement.getText();
        } catch (Exception e) {
            System.out.println("No error message found for invalid voucher.");
            return null;
        }
    }
}
