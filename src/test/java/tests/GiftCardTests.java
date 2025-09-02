package tests;

import base.DriverSetup;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CityPage;
import pages.GiftCardPage;

public class GiftCardTests {
    private GiftCardPage giftHandler;
    private CityPage cityHandler;

    @BeforeClass
    public void setup() {
        DriverSetup.initDriver();
        giftHandler = new GiftCardPage();
        cityHandler = new CityPage();
        cityHandler.chooseCity("Bengaluru");
    }

    @Test(priority = 1)
    public void testGiftCardSection() {
        boolean result = giftHandler.openGiftCardsAndVerifyBalance();
        System.out.println(result
                ? "Gift Card section opened and balance icon visible."
                : "Could not validate Gift Card section.");
    }

    @Test(priority = 2)
    public void testInvalidVoucherError() {
        String errorMessage = giftHandler.enterInvalidVoucherAndFetchError("INVALID123");
        if (errorMessage != null) {
            System.out.println("Error message captured: " + errorMessage);
        } else {
            System.out.println("No error message found for invalid voucher.");
        }
    }

    @AfterClass
    public void tearDown() {
        DriverSetup.quitDriver();
    }
}
