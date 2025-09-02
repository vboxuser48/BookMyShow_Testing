package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import utils.ConfigReader;

import java.time.Duration;

public class DriverSetup {
    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    public static void initDriver() {
        if (tlDriver.get() == null) {
            String browser = ConfigReader.get("browser");
            if (browser == null || browser.isEmpty()) {
                browser = "chrome"; // fallback
            }

            WebDriver driver;

            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver();
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                    break;
                default:
                    throw new RuntimeException("Browser not supported: " + browser);
            }

            tlDriver.set(driver);

            // Maximize & set timeout
            tlDriver.get().manage().window().maximize();
            tlDriver.get().manage().timeouts()
                    .implicitlyWait(Duration.ofSeconds(Long.parseLong(ConfigReader.get("timeout"))));

            // Navigate to baseUrl
            String baseUrl = ConfigReader.get("baseUrl");
            if (baseUrl != null && !baseUrl.isEmpty()) {
                tlDriver.get().get(baseUrl);
            }
        }
    }

    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    public static void quitDriver() {
        if (tlDriver.get() != null) {
            tlDriver.get().quit();
            tlDriver.remove();
        }
    }
}
