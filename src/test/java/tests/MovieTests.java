package tests;

import base.DriverSetup;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CityPage;
import pages.MoviePage;

public class MovieTests {
    private MoviePage movieHandler;

    @BeforeClass
    public void setup() {
        DriverSetup.initDriver();
        CityPage cityHandler = new CityPage();
        boolean citySelected = cityHandler.chooseCity("Mumbai");
        Assert.assertTrue(citySelected, "Failed to select city before starting movie tests.");
        movieHandler = new MoviePage();
    }

    @Test(priority = 1)
    public void testComingSoonSection() {
        movieHandler.ensureMoviesPage();
        boolean visible = movieHandler.checkComingSoon();
        Assert.assertTrue(visible, "'Coming Soon' section should be visible.");
    }

    @Test(priority = 2)
    public void testUpcomingMoviesNavigation() {
        movieHandler.ensureMoviesPage();
        movieHandler.viewUpcomingMovies();
        Assert.assertFalse(movieHandler.isBlockedByCloudflare(),
                "Blocked by Cloudflare while exploring upcoming movies!");
    }

    @Test(priority = 3)
    public void testSelectFirstMovie() {
        movieHandler.ensureMoviesPage();
        movieHandler.selectFirstMovie();
        Assert.assertFalse(movieHandler.isBlockedByCloudflare(),
                "Blocked by Cloudflare while selecting running movie!");
    }

    @AfterClass
    public void tearDown() {
        DriverSetup.quitDriver();
    }
}
