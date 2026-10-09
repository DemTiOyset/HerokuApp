package org.example.herokuapp.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

/**
 * Base class that owns the WebDriver lifecycle.
 *
 * The driver is created before every test method and is always closed after it
 * ({@code alwaysRun = true}), so a failing test cannot leave a Chrome process behind.
 *
 * Run headless (default):   mvn test
 * Run with a visible window: mvn test -Dheadless=false
 * Slow network:              mvn test -Dtimeout.seconds=30
 */
public abstract class TestBase {

    protected static final Duration TIMEOUT = Duration.ofSeconds(
            Integer.parseInt(System.getProperty("timeout.seconds", "30")));

    /**
     * The demo site is hosted on Heroku and sometimes "wakes up" for about 10 seconds,
     * so the page load limit is deliberately higher than the explicit wait timeout.
     */
    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(
            Integer.parseInt(System.getProperty("page.load.timeout.seconds", "60")));

    private static final boolean HEADLESS = !"false".equalsIgnoreCase(System.getProperty("headless", "true"));

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        if (HEADLESS) {
            options.addArguments("--headless=new");
        }
        options.addArguments(
                "--window-size=1920,1080",
                "--disable-gpu",
                "--no-sandbox",
                "--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT);
        wait = new WebDriverWait(driver, TIMEOUT);

        System.out.println("[setUp] Chrome started, headless=" + HEADLESS);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("[tearDown] Chrome closed");
        }
    }
}
