package tests;

import base.DriverSetup;
import utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;
import pages.CityPage;
import utils.WaitUtils;
import locators.LocatorRepository;

public class LoginTests {
    private LoginPage loginPage;
    private CityPage cityPage;

    @BeforeClass
    public void setup() {
        DriverSetup.initDriver();

        // ensure homepage ready
        WaitUtils.visible(LocatorRepository.get("signInBtn"), 15);

        // select city once
        cityPage = new CityPage();
        cityPage.selectCity(ConfigReader.get("city"));

        loginPage = new LoginPage();
        System.out.println("✅ Setup complete → Test session ready.");
    }

    @Test(priority = 1)
    public void invalidLoginTest() {
        boolean result = loginPage.signInInvalid(ConfigReader.get("invalidMobile"));
        Assert.assertTrue(result, "❌ Invalid login test failed.");
    }

    @Test(priority = 2)
    public void validLoginTest() {
        boolean result = loginPage.validSignIn(ConfigReader.get("validMobile"), ConfigReader.get("validOtp"));
        Assert.assertTrue(result, "❌ Valid login test failed.");
    }

    @Test(priority = 3)
    public void loginUIVerification() {
        boolean result = loginPage.verifyUI(ConfigReader.get("validMobile"), ConfigReader.get("validOtp"));
        Assert.assertTrue(result, "❌ Login UI verification failed.");
    }

    @AfterClass
    public void cleanup() {
        DriverSetup.quitDriver();
        System.out.println("🧹 Browser session closed after all tests.");
    }
}
