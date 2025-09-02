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
    private final WebDriver webDriver;
    private final WebDriverWait webWait;

    public MoviePage() {
        this.webDriver = DriverSetup.getDriver();
        this.webWait = new WebDriverWait(webDriver, Duration.ofSeconds(15));
        System.out.println("MoviePage initialized with WebDriver.");
    }

    private void performClick(WebElement element) {
        try {
            webWait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (Exception e) {
            System.out.println("Normal click failed, retrying with JS...");
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element);
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", element);
        }
    }

    public boolean isBlockedByCloudflare() {
        try {
            return webDriver.getPageSource().contains("Sorry, you have been blocked");
        } catch (Exception e) {
            return false;
        }
    }

    private void savePageSource(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(webDriver.getPageSource());
            System.out.println("Page source logged: " + fileName);
        } catch (IOException e) {
            System.out.println("Failed to write page source: " + e.getMessage());
        }
    }

    public void openMoviesPage() {
        WebElement moviesTab = WaitUtils.clickable(LocatorRepository.get("moviesTab"), 15);
        performClick(moviesTab);
        webWait.until(ExpectedConditions.presenceOfElementLocated(LocatorRepository.get("movieTile")));
        System.out.println("Movies page loaded.");
    }

    public void ensureMoviesPage() {
        try {
            if (!webDriver.getCurrentUrl().contains("/movies")) {
                openMoviesPage();
            }
        } catch (Exception e) {
            openMoviesPage();
        }
    }

    public void selectFirstMovie() {
        ensureMoviesPage();
        WebElement firstMovie = WaitUtils.clickable(LocatorRepository.get("movieTile"), 15);
        performClick(firstMovie);
        System.out.println("Opened first running movie.");
        if (isBlockedByCloudflare()) savePageSource("cloudflare_block_runningMovie.html");
    }

    public void openMovie(String movieName) {
        ensureMoviesPage();
        By locator = By.xpath(String.format("//a[starts-with(@href,'/movies/') and normalize-space(text())='%s']", movieName));
        WebElement movie = WaitUtils.clickable(locator, 15);
        performClick(movie);
        System.out.println("Opened movie: " + movieName);
        if (isBlockedByCloudflare()) savePageSource("cloudflare_block_movieByName.html");
    }

    public boolean checkComingSoon() {
        ensureMoviesPage();
        try {
            WebElement section = WaitUtils.visible(LocatorRepository.get("comingSoonSection"), 15);
            boolean visible = section.isDisplayed();
            System.out.println("'Coming Soon' section is visible.");
            return visible;
        } catch (Exception e) {
            System.out.println("'Coming Soon' section not found.");
            return false;
        }
    }

    public void viewUpcomingMovies() {
        ensureMoviesPage();
        WebElement upcoming = WaitUtils.clickable(LocatorRepository.get("exploreUpcomingMovies"), 15);
        performClick(upcoming);
        System.out.println("Navigated to Upcoming Movies.");
        if (isBlockedByCloudflare()) savePageSource("cloudflare_block_upcoming.html");
    }
}
