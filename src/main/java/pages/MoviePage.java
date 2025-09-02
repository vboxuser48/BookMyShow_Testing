package pages;

import base.DriverSetup;
import locators.LocatorRepository;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

public class MoviePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public MoviePage() {
        this.driver = DriverSetup.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        System.out.println("✅ MoviePage initialized with WebDriver instance.");
    }

    // ---------- Utility: Safe click ----------
    private void safeClick(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (Exception e) {
            System.out.println("⚠️ Normal click failed, retrying with JS...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    // ---------- Utility: Cloudflare detection ----------
    public boolean isCloudflareBlocked() {
        try {
            return driver.getPageSource().contains("Sorry, you have been blocked");
        } catch (Exception e) {
            return false;
        }
    }

    // ---------- Utility: Log page source if blocked ----------
    private void logPageSource(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(driver.getPageSource());
            System.out.println("📄 Page source logged: " + fileName);
        } catch (IOException e) {
            System.out.println("❌ Failed to write page source: " + e.getMessage());
        }
    }

    // ---------- Go to Movies page ----------
    public void goToMoviesPage() {
        WebElement moviesTab = WaitUtils.clickable(LocatorRepository.get("moviesTab"), 15);
        safeClick(moviesTab);
        wait.until(ExpectedConditions.presenceOfElementLocated(LocatorRepository.get("movieTile")));
        System.out.println("🎬 Movies page loaded.");
    }

    // ---------- Ensure we are on Movies page ----------
    public void ensureOnMoviesPage() {
        try {
            if (!driver.getCurrentUrl().contains("/movies")) {
                goToMoviesPage();
            }
        } catch (Exception e) {
            goToMoviesPage();
        }
    }

    // ---------- Select first running movie ----------
    public void selectRunningMovie() {
        ensureOnMoviesPage();
        WebElement firstMovie = WaitUtils.clickable(LocatorRepository.get("movieTile"), 15);
        safeClick(firstMovie);
        System.out.println("✅ Opened first running movie.");
        if (isCloudflareBlocked()) logPageSource("cloudflare_block_runningMovie.html");
    }

    // ---------- Open specific movie by name ----------
    public void openMovieByName(String movieName) {
        ensureOnMoviesPage();
        By movieLocator = By.xpath(String.format("//a[starts-with(@href,'/movies/') and normalize-space(text())='%s']", movieName));
        WebElement movie = WaitUtils.clickable(movieLocator, 15);
        safeClick(movie);
        System.out.println("✅ Opened movie: " + movieName);
        if (isCloudflareBlocked()) logPageSource("cloudflare_block_movieByName.html");
    }

    // ---------- Validate Coming Soon section ----------
    public boolean validateComingSoonSection() {
        ensureOnMoviesPage();
        try {
            WebElement comingSoon = WaitUtils.visible(LocatorRepository.get("comingSoonSection"), 15);
            boolean displayed = comingSoon.isDisplayed();
            System.out.println("✅ 'Coming Soon' section is visible.");
            return displayed;
        } catch (Exception e) {
            System.out.println("❌ 'Coming Soon' section not found.");
            return false;
        }
    }

    // ---------- Explore Upcoming Movies ----------
    public void exploreUpcomingMovies() {
        ensureOnMoviesPage();
        WebElement upcoming = WaitUtils.clickable(LocatorRepository.get("exploreUpcomingMovies"), 15);
        safeClick(upcoming);
        System.out.println("✅ Navigated to Upcoming Movies.");
        if (isCloudflareBlocked()) logPageSource("cloudflare_block_upcoming.html");
    }
}
