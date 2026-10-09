package org.example.herokuapp.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for https://the-internet.herokuapp.com/add_remove_elements/
 *
 * "Add Element" appends a "Delete" button to the #elements container,
 * every "Delete" button removes itself.
 */
public class AddRemoveElementsPage extends BasePage {

    private static final By ADD_ELEMENT_BUTTON = By.xpath("//button[text()='Add Element']");
    private static final By DELETE_BUTTON = By.xpath("//button[text()='Delete']");
    private static final By ADDED_ELEMENTS = By.cssSelector("#elements button");

    public AddRemoveElementsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    protected String getPath() {
        return "/add_remove_elements/";
    }

    @Override
    protected void waitForPageLoaded() {
        wait.until(ExpectedConditions.elementToBeClickable(ADD_ELEMENT_BUTTON));
        waitUntilAddHandlerIsReady();
    }

    /**
     * The page declares {@code addElement()} in an inline script that sits BELOW the button,
     * so the button becomes clickable a moment before its handler exists. A click in that
     * window does nothing at all, therefore we wait for the function itself.
     */
    private void waitUntilAddHandlerIsReady() {
        wait.until(d -> Boolean.TRUE.equals(
                ((JavascriptExecutor) d).executeScript("return typeof addElement === 'function'")));
    }

    /**
     * Clicks "Add Element" once and waits until the new element really appears,
     * so a lost click cannot silently pass to the next step.
     */
    public AddRemoveElementsPage addElement() {
        int countBefore = getElementsCount();
        wait.until(ExpectedConditions.elementToBeClickable(ADD_ELEMENT_BUTTON)).click();
        wait.until(d -> getElementsCount() == countBefore + 1);
        return this;
    }

    /** Clicks "Add Element" the given number of times. */
    public AddRemoveElementsPage addElements(int count) {
        for (int i = 0; i < count; i++) {
            addElement();
        }
        return this;
    }

    /** Clicks the first "Delete" button on the page. */
    public AddRemoveElementsPage deleteElement() {
        wait.until(ExpectedConditions.elementToBeClickable(DELETE_BUTTON)).click();
        return this;
    }

    /** Number of the added elements (every one of them carries a "Delete" button). */
    public int getElementsCount() {
        return driver.findElements(ADDED_ELEMENTS).size();
    }

    /** Number of "Delete" buttons currently rendered on the page. */
    public int getDeleteButtonsCount() {
        return driver.findElements(DELETE_BUTTON).size();
    }

    /** Explicit wait until the number of the added elements becomes the expected one. */
    public AddRemoveElementsPage waitUntilElementsCountIs(int expected) {
        wait.until(ExpectedConditions.numberOfElementsToBe(ADDED_ELEMENTS, expected));
        return this;
    }
}
