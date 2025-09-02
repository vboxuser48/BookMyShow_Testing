package pages;

import base.DriverSetup;
import locators.LocatorRepository;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

public class GiftCardPage {
    private final WebDriver driver;

    public GiftCardPage() {
        this.driver = DriverSetup.getDriver();
        System.out.println("✅ GiftCardPage initialized with WebDriver instance.");
    }

    private void closePopupIfPresent() {
        try {
            WebElement popup = driver.findElement(LocatorRepository.get("popupClose"));
            if (popup.isDisplayed()) {
                System.out.println("↩️ Closing popup overlay before clicking Gift Cards...");
                popup.click();
                Thread.sleep(1000);
            }
        } catch (Exception ignored) {}
    }

    public boolean openGiftCardsAndValidateCheckBalanceVisible() {
        try {
            System.out.println("🎁 Opening Gift Cards section...");
            closePopupIfPresent();
            WaitUtils.clickable(LocatorRepository.get("giftTab"), 10).click();

            WebElement balance = WaitUtils.clickable(LocatorRepository.get("checkBalanceBtn"), 10);
            boolean visible = balance.isDisplayed();
            if (visible) {
                System.out.println("✅ 'Check Gift Card Balance' icon is visible.");
            }
            return visible;
        } catch (Exception e) {
            System.out.println("❌ Failed to validate Gift Cards section: " + e.getMessage());
            return false;
        }
    }

    public String checkInvalidVoucherAndGetError(String voucher) {
        try {
            System.out.println("📝 Clicking on 'Check Balance' icon...");
            closePopupIfPresent();
            WaitUtils.clickable(LocatorRepository.get("checkBalanceBtn"), 10).click();

            WebElement input = WaitUtils.visible(LocatorRepository.get("voucherInput"), 10);
            input.clear();
            input.sendKeys(voucher);
            System.out.println("Entered invalid voucher: " + voucher);

            WebElement error = WaitUtils.visible(LocatorRepository.get("errorGiftVoucher"), 10);
            return error.getText();
        } catch (Exception e) {
            System.out.println("❌ No error message found for invalid voucher.");
            return null;
        }
    }
}
