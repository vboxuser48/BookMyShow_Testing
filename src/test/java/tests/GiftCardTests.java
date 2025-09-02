package tests;

import base.DriverSetup;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CityPage;
import pages.GiftCardPage;

public class GiftCardTests {
    private GiftCardPage giftCardPage;
    private CityPage cityPage;

    @BeforeClass
    public void setup() {
        DriverSetup.initDriver();
        giftCardPage = new GiftCardPage();
        cityPage = new CityPage();

        // ✅ Ensure we select a city before going to Gift Card section
        cityPage.selectCity("Bengaluru");
    }

    @Test(priority = 1)
    public void testGiftCardSectionAndBalanceIcon() {
        boolean visible = giftCardPage.openGiftCardsAndValidateCheckBalanceVisible();
        System.out.println(visible
                ? "✅ Gift Card section opened and icon validated."
                : "❌ Could not validate Gift Card section.");
    }

    @Test(priority = 2)
    public void testInvalidVoucherBalance() {
        String errorMsg = giftCardPage.checkInvalidVoucherAndGetError("INVALID123");
        if (errorMsg != null) {
            System.out.println("✅ Error message captured successfully: " + errorMsg);
        } else {
            System.out.println("❌ No error message found for invalid voucher.");
        }
    }

    @AfterClass
    public void tearDown() {
        DriverSetup.quitDriver();
    }
}
