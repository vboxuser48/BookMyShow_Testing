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
    private LoginPage loginHandler;
    private CityPage cityHandler;

    @BeforeClass
    public void setup() {
        DriverSetup.initDriver();
        WaitUtils.visible(LocatorRepository.get("signInBtn"), 15);

        cityHandler = new CityPage();
        cityHandler.chooseCity(ConfigReader.get("city"));

        loginHandler = new LoginPage();
        System.out.println("Setup complete → Test session ready.");
    }

    @Test(priority = 1)
    public void testInvalidLogin() {
        boolean result = loginHandler.attemptInvalidLogin(ConfigReader.get("invalidMobile"));
        Assert.assertTrue(result, "Invalid login test failed.");
    }

    @Test(priority = 2)
    public void testValidLogin() {
        boolean result = loginHandler.attemptValidLogin(ConfigReader.get("validMobile"), ConfigReader.get("validOtp"));
        Assert.assertTrue(result, "Valid login test failed.");
    }

    @Test(priority = 3)
    public void testLoginUI() {
        boolean result = loginHandler.verifyLoginUI(ConfigReader.get("validMobile"), ConfigReader.get("validOtp"));
        Assert.assertTrue(result, "Login UI verification failed.");
    }

    @AfterClass
    public void cleanup() {
        DriverSetup.quitDriver();
        System.out.println("Browser session closed after all tests.");
    }
}
