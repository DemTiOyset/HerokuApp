package org.example.herokuapp.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for https://the-internet.herokuapp.com/inputs
 *
 * The page holds a single {@code <input type=number>}: the browser itself accepts digits
 * and the arrow keys, and ignores letters.
 */
public class InputsPage extends BasePage {

    private static final By NUMBER_INPUT = By.tagName("input");

    public InputsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    protected String getPath() {
        return "/inputs";
    }

    @Override
    protected void waitForPageLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(NUMBER_INPUT));
    }

    public WebElement getNumberInput() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(NUMBER_INPUT));
    }

    /** Current value of the field. */
    public String getValue() {
        return getNumberInput().getAttribute("value");
    }

    /** Clears the field and types the given text. */
    public InputsPage typeValue(String value) {
        WebElement input = getNumberInput();
        input.clear();
        input.sendKeys(value);
        return this;
    }

    /** Sends arbitrary keys to the field, for example {@code Keys.ARROW_UP}. */
    public InputsPage pressKeys(CharSequence... keys) {
        getNumberInput().sendKeys(keys);
        return this;
    }

    public InputsPage pressArrowUp() {
        return pressKeys(Keys.ARROW_UP);
    }

    public InputsPage pressArrowDown() {
        return pressKeys(Keys.ARROW_DOWN);
    }

    /** Explicit wait until the field shows the expected value. */
    public InputsPage waitUntilValueIs(String expected) {
        wait.until(d -> expected.equals(getValue()));
        return this;
    }

    /** Explicit wait until the field has any non-empty value. */
    public InputsPage waitUntilValueIsNotEmpty() {
        wait.until(d -> !getValue().isEmpty());
        return this;
    }
}
