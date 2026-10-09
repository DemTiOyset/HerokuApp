package org.example.herokuapp.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Page Object for https://the-internet.herokuapp.com/checkboxes
 *
 * The page contains two checkboxes: the first one is unchecked, the second one is checked.
 */
public class CheckboxesPage extends BasePage {

    private static final By CHECKBOXES = By.cssSelector("[type=checkbox]");

    public CheckboxesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    protected String getPath() {
        return "/checkboxes";
    }

    @Override
    protected void waitForPageLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(CHECKBOXES));
    }

    public List<WebElement> getCheckboxes() {
        return driver.findElements(CHECKBOXES);
    }

    public int getCheckboxesCount() {
        return getCheckboxes().size();
    }

    /** Checkbox by its order on the page, starting from 0. */
    public WebElement getCheckbox(int index) {
        return getCheckboxes().get(index);
    }

    public boolean isChecked(int index) {
        return getCheckbox(index).isSelected();
    }

    /** Clicks a checkbox only when its current state differs from the wanted one. */
    public CheckboxesPage setChecked(int index, boolean checked) {
        WebElement checkbox = getCheckbox(index);
        if (checkbox.isSelected() != checked) {
            checkbox.click();
        }
        return this;
    }

    public CheckboxesPage check(int index) {
        return setChecked(index, true);
    }

    public CheckboxesPage uncheck(int index) {
        return setChecked(index, false);
    }

    /** Explicit wait until the checkbox reaches the expected state. */
    public CheckboxesPage waitUntilChecked(int index, boolean expected) {
        wait.until(d -> getCheckbox(index).isSelected() == expected);
        return this;
    }
}
