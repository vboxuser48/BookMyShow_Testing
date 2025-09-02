package tests;

import base.DriverSetup;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CityPage;

public class CityTests {
    private CityPage cityPage;

    @BeforeClass
    public void setup() {
        DriverSetup.initDriver();
        cityPage = new CityPage();
    }

    @Test(priority = 1)
    public void testSelectCity() {
        boolean result = cityPage.selectCity("Bengaluru"); // ✅ pass city name
        Assert.assertTrue(result, "❌ City selection failed!");
    }

    @Test(priority = 2)
    public void testChangeLocation() {
        boolean result = cityPage.changeLocation("Mumbai"); // ✅ example city
        Assert.assertTrue(result, "❌ Change location failed!");
    }

    @Test(priority = 3)
    public void testSelectCityByIcon() {
        boolean result = cityPage.selectCityByIcon("Delhi"); // ✅ example city
        Assert.assertTrue(result, "❌ Select city by icon failed!");
    }

    @Test(priority = 4)
    public void testViewAllCitiesToggle() {
        boolean result = cityPage.viewAllCitiesToggle();
        Assert.assertTrue(result, "❌ No cities found in 'View All Cities'");
    }

    @AfterClass
    public void tearDown() {
        DriverSetup.quitDriver();
    }
}
