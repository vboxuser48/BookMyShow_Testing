package tests;

import base.DriverSetup;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CityPage;

public class CityTests {
    private CityPage cityHandler;

    @BeforeClass
    public void setup() {
        DriverSetup.initDriver();
        cityHandler = new CityPage();
    }

    @Test(priority = 1)
    public void testChooseCity() {
        boolean result = cityHandler.chooseCity("Bengaluru");
        Assert.assertTrue(result, "City selection failed!");
    }

    @Test(priority = 2)
    public void testUpdateLocation() {
        boolean result = cityHandler.updateLocation("Mumbai");
        Assert.assertTrue(result, "Change location failed!");
    }

    @Test(priority = 3)
    public void testChooseCityByIcon() {
        boolean result = cityHandler.chooseCityByIcon("Delhi");
        Assert.assertTrue(result, "Select city by icon failed!");
    }

    @Test(priority = 4)
    public void testExpandAllCities() {
        boolean result = cityHandler.expandAllCities();
        Assert.assertTrue(result, "No cities found in 'View All Cities'");
    }

    @AfterClass
    public void tearDown() {
        DriverSetup.quitDriver();
    }
}
