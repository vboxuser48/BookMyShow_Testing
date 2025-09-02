package pages;

import base.DriverSetup;
import locators.LocatorRepository;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

import java.util.List;

public class LoginPage {
    private final WebDriver driver;
    private final int DEFAULT_TIMEOUT = 12;

    public LoginPage() {
        this.driver = DriverSetup.getDriver();
        PageFactory.initElements(driver, this);
        System.out.println("✅ LoginPage initialized.");
    }

    /** Safe click with JS fallback */
    private void safeClick(By locator) {
        try {
            WaitUtils.clickable(locator, DEFAULT_TIMEOUT).click();
        } catch (Exception e) {
            System.out.println("⚠️ Normal click failed, retrying with JS...");
            WebElement el = driver.findElement(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    /** Type like a human */
    private void typeSlowly(WebElement element, String text) {
        if (element == null || text == null) return;
        for (char c : text.toCharArray()) {
            element.sendKeys(Character.toString(c));
            try {
                Thread.sleep(80 + (int) (Math.random() * 60));
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /** 🚨 Invalid login */
    public boolean signInInvalid(String invalidMobile) {
        try {
            System.out.println("➡️ signInInvalid: starting...");

            safeClick(LocatorRepository.get("signInBtn"));

            WebElement mobileBox = WaitUtils.visible(LocatorRepository.get("mobileNumberBox"), DEFAULT_TIMEOUT);
            mobileBox.clear();
            typeSlowly(mobileBox, invalidMobile);

            safeClick(LocatorRepository.get("continueBtn"));

            // actual error div you provided
            By errorLocator = By.xpath("//div[contains(@class,'sc-z1ldnh-12') and text()='Invalid mobile number']");
            WebElement errorMsg = WaitUtils.visible(errorLocator, DEFAULT_TIMEOUT);

            return errorMsg != null && errorMsg.isDisplayed();
        } catch (Exception e) {
            System.err.println("❌ signInInvalid failed → " + e.getMessage());
            return false;
        }
    }

    /** 🔐 Valid login (mobile + OTP) */
    public boolean validSignIn(String validMobile, String otp) {
        try {
            System.out.println("➡️ validSignIn: starting...");

            safeClick(LocatorRepository.get("signInBtn"));

            WebElement mobileBox = WaitUtils.visible(LocatorRepository.get("mobileNumberBox"), DEFAULT_TIMEOUT);
            mobileBox.clear();
            typeSlowly(mobileBox, validMobile);

            safeClick(LocatorRepository.get("continueBtn"));

            // wait for OTP fields
            List<WebElement> otpInputs = driver.findElements(LocatorRepository.get("otpInputs"));
            if (otpInputs.isEmpty()) {
                System.out.println("⚠️ OTP fields not found.");
                return false;
            }

            for (int i = 0; i < otp.length() && i < otpInputs.size(); i++) {
                otpInputs.get(i).sendKeys(String.valueOf(otp.charAt(i)));
            }

            System.out.println("✅ OTP entered.");
            return true;
        } catch (Exception e) {
            System.err.println("❌ validSignIn failed → " + e.getMessage());
            return false;
        }
    }

    /** 🔍 Verify UI */
    public boolean verifyUI(String validMobile, String otp) {
        System.out.println("🔎 Checking Sign In UI...");
        return validSignIn(validMobile, otp);
    }
}
