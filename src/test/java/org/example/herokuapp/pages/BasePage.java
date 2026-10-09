package org.example.herokuapp.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Common part of every Page Object: the base URL, the driver and explicit waits.
 *
 * Page Objects keep locators and page actions in one place, so tests read like steps
 * of a scenario and do not contain CSS/XPath expressions.
 */
public abstract class BasePage {

    protected static final String BASE_URL = "https://the-internet.herokuapp.com";

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /** Relative address of the page, for example {@code /checkboxes}. */
    protected abstract String getPath();

    /** Opens the page and waits until its content is loaded. */
    public void open() {
        driver.get(BASE_URL + getPath());
        waitUntilPageIsReady();
        waitForPageLoaded();
    }

    /**
     * Explicit wait until the browser has parsed the document and executed all inline scripts.
     *
     * Some pages of the demo site declare their JavaScript handlers below the elements that
     * use them, so an element can look clickable while its handler does not exist yet.
     */
    private void waitUntilPageIsReady() {
        wait.until(d -> "complete".equals(
                ((JavascriptExecutor) d).executeScript("return document.readyState")));
    }

    /** Explicit wait for the page content instead of an implicit sleep. */
    protected abstract void waitForPageLoaded();
}
