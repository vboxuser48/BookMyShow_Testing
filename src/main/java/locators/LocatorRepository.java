package locators;

import org.openqa.selenium.By;
import java.util.HashMap;
import java.util.Map;

public final class LocatorRepository {
    private static final Map<String, By> L = new HashMap<>();

    static {
        L.put("signInBtn", By.xpath("//div[normalize-space()='Sign in']"));
        L.put("mobileNumberBox", By.xpath("//input[@type='tel']"));
        L.put("continueBtn", By.xpath("//div[contains(@class,'sc-') and text()='Continue']"));
        L.put("invalidErrorMsg", By.xpath("//div[contains(@class,'sc-z1ldnh-12') and text()='Invalid mobile number']"));
        L.put("otpInputs", By.xpath("//input[@type='tel']"));

        L.put("cityInput", By.xpath("//input[@placeholder='Search for your city']"));
        L.put("errorMsg", By.xpath("//div[text()='No results found.']"));
        L.put("citySuggestionBangalore", By.xpath("//strong[text()='Bengaluru']/ancestor::li"));
        L.put("allCities", By.xpath("//p[contains(text(),'View All Cities')]"));
        L.put("hideCities", By.xpath("//p[contains(text(),'Hide all cities')]"));
        L.put("cityList", By.xpath("//ul[contains(@class,'sc-yuf6si-1')]//li"));
        L.put("popularCity", By.xpath("//ul[contains(@class,'sc-p6ayv6-1')]//p"));
        L.put("popupClose", By.id("bottomSheet-model-close"));

        L.put("moviesTab", By.xpath("//a[text()='Movies']"));
        L.put("movieTile", By.xpath("(//a[contains(@href,'/movies/') and contains(@class,'sc-')])[1]"));
        L.put("comingSoonSection", By.xpath("//a[contains(@href,'upcoming-movies')]"));
        L.put("exploreUpcomingMovies", By.xpath("//a[contains(@href,'upcoming-movies')]"));

        L.put("searchTrigger", By.xpath("//span[contains(text(),'Search for Movies')]"));
        L.put("searchInput", By.xpath("//input[@placeholder='Search for Movies, Events, Plays & more']"));
        L.put("topSearchResult", By.xpath("(//span[contains(@class,'sc-f42fb7-2')])[1]"));

        L.put("giftTab", By.xpath("//a[text()='Gift Cards']"));
        L.put("checkBalanceBtn", By.xpath("//div[contains(text(),'Check Gift Card Balance')]"));
        L.put("voucherInput", By.id("gift-voucher"));
        L.put("errorGiftVoucher", By.xpath("//*[contains(@id,'error-gift-voucher') or contains(text(),'Invalid')]"));
    }

    private LocatorRepository() {}

    public static By get(String key) {
        return L.get(key);
    }
}
