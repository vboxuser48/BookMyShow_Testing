package pages;

import base.DriverSetup;
import locators.LocatorRepository;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

import java.util.List;

public class LoginPage {
    private final WebDriver webDriver;
    private final int DEFAULT_TIMEOUT = 12;

    public LoginPage() {
        this.webDriver = DriverSetup.getDriver();
        PageFactory.initElements(webDriver, this);
        System.out.println("LoginPage initialized.");
    }

    private void performClick(By locator) {
        try {
            WaitUtils.clickable(locator, DEFAULT_TIMEOUT).click();
        } catch (Exception e) {
            System.out.println("Normal click failed, retrying with JS...");
            WebElement element = webDriver.findElement(locator);
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", element);
        }
    }

    private void enterTextSlowly(WebElement element, String text) {
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

    public boolean attemptInvalidLogin(String mobile) {
        try {
            System.out.println("Starting invalid login test...");
            performClick(LocatorRepository.get("signInBtn"));

            WebElement inputBox = WaitUtils.visible(LocatorRepository.get("mobileNumberBox"), DEFAULT_TIMEOUT);
            inputBox.clear();
            enterTextSlowly(inputBox, mobile);

            performClick(LocatorRepository.get("continueBtn"));

            By errorLocator = By.xpath("//div[contains(@class,'sc-z1ldnh-12') and text()='Invalid mobile number']");
            WebElement errorMsg = WaitUtils.visible(errorLocator, DEFAULT_TIMEOUT);

            return errorMsg != null && errorMsg.isDisplayed();
        } catch (Exception e) {
            System.err.println("Invalid login failed → " + e.getMessage());
            return false;
        }
    }

    public boolean attemptValidLogin(String mobile, String otp) {
        try {
            System.out.println("Starting valid login test...");
            performClick(LocatorRepository.get("signInBtn"));

            WebElement inputBox = WaitUtils.visible(LocatorRepository.get("mobileNumberBox"), DEFAULT_TIMEOUT);
            inputBox.clear();
            enterTextSlowly(inputBox, mobile);

            performClick(LocatorRepository.get("continueBtn"));

            List<WebElement> otpInputs = webDriver.findElements(LocatorRepository.get("otpInputs"));
            if (otpInputs.isEmpty()) {
                System.out.println("OTP fields not found.");
                return false;
            }

            for (int i = 0; i < otp.length() && i < otpInputs.size(); i++) {
                otpInputs.get(i).sendKeys(String.valueOf(otp.charAt(i)));
            }

            System.out.println("OTP entered successfully.");
            return true;
        } catch (Exception e) {
            System.err.println("Valid login failed → " + e.getMessage());
            return false;
        }
    }

    public boolean verifyLoginUI(String mobile, String otp) {
        System.out.println("Checking Sign In UI...");
        return attemptValidLogin(mobile, otp);
    }
}
