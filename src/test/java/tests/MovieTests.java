package tests;

import base.DriverSetup;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CityPage;
import pages.MoviePage;

public class MovieTests {
    private MoviePage moviePage;

    @BeforeClass
    public void setUp() {
        DriverSetup.initDriver();
        CityPage cityPage = new CityPage();
        boolean citySelected = cityPage.selectCity("Mumbai");
        Assert.assertTrue(citySelected, "❌ Failed to select city before starting movie tests.");
        moviePage = new MoviePage();
    }

    @Test(priority = 1)
    public void testValidateComingSoonSection() {
        moviePage.ensureOnMoviesPage();
        boolean isDisplayed = moviePage.validateComingSoonSection();
        Assert.assertTrue(isDisplayed, "❌ 'Coming Soon' section should be visible.");
    }

    @Test(priority = 2)
    public void testExploreUpcomingMovies() {
        moviePage.ensureOnMoviesPage();
        moviePage.exploreUpcomingMovies();
        Assert.assertFalse(moviePage.isCloudflareBlocked(),
                "❌ Blocked by Cloudflare while exploring upcoming movies!");
    }

    @Test(priority = 3)  // 👈 runs last to avoid breaking other tests
    public void testSelectRunningMovie() {
        moviePage.ensureOnMoviesPage();
        moviePage.selectRunningMovie();
        Assert.assertFalse(moviePage.isCloudflareBlocked(),
                "❌ Blocked by Cloudflare while selecting running movie!");
    }

    @AfterClass
    public void tearDown() {
        DriverSetup.quitDriver();
    }
}
