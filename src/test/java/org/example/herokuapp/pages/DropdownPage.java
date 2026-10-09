package org.example.herokuapp.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object for https://the-internet.herokuapp.com/dropdown
 *
 * The list contains a disabled placeholder "Please select an option" and two options.
 */
public class DropdownPage extends BasePage {

    private static final By DROPDOWN = By.id("dropdown");

    public static final String PLACEHOLDER = "Please select an option";
    public static final String OPTION_1 = "Option 1";
    public static final String OPTION_2 = "Option 2";

    public DropdownPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    protected String getPath() {
        return "/dropdown";
    }

    @Override
    protected void waitForPageLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(DROPDOWN));
    }

    public Select getSelect() {
        return new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(DROPDOWN)));
    }

    /** All the options of the list, in the order they are rendered. */
    public List<String> getOptionTexts() {
        return getSelect().getOptions().stream()
                .map(WebElement::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public List<WebElement> getOptions() {
        return getSelect().getOptions();
    }

    public DropdownPage selectByVisibleText(String text) {
        getSelect().selectByVisibleText(text);
        return this;
    }

    public DropdownPage selectByValue(String value) {
        getSelect().selectByValue(value);
        return this;
    }

    public DropdownPage selectByIndex(int index) {
        getSelect().selectByIndex(index);
        return this;
    }

    public String getSelectedText() {
        return getSelect().getFirstSelectedOption().getText().trim();
    }

    /** True when the option with the given text is disabled and cannot be chosen. */
    public boolean isOptionDisabled(String text) {
        return getOptions().stream()
                .filter(option -> option.getText().trim().equals(text))
                .findFirst()
                .map(option -> !option.isEnabled())
                .orElseThrow(() -> new AssertionError("Option not found: " + text));
    }

    /** Explicit wait until the given option becomes the selected one. */
    public DropdownPage waitUntilSelectedTextIs(String expected) {
        wait.until(d -> getSelectedText().equals(expected));
        return this;
    }
}
