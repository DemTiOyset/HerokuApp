package org.example.herokuapp.tests;

import org.example.herokuapp.base.TestBase;
import org.example.herokuapp.pages.DropdownPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Tests for the "Dropdown List" page.
 *
 * Positive cases: all the options are present, the first and the second option can be chosen.
 * Negative case: the placeholder option is disabled and cannot be a real choice.
 */
public class DropdownTest extends TestBase {

    @Test(description = "The list contains the placeholder and both options")
    public void shouldContainAllExpectedOptions() {
        DropdownPage page = new DropdownPage(driver, wait);
        page.open();

        List<String> options = page.getOptionTexts();
        System.out.println("[check] options: " + options);

        Assert.assertEquals(options,
                List.of(DropdownPage.PLACEHOLDER, DropdownPage.OPTION_1, DropdownPage.OPTION_2),
                "The dropdown must contain the placeholder and two options in this order");
    }

    @Test(description = "The first option can be selected and becomes the selected one")
    public void shouldSelectFirstOption() {
        DropdownPage page = new DropdownPage(driver, wait);
        page.open();

        page.selectByIndex(1).waitUntilSelectedTextIs(DropdownPage.OPTION_1);
        System.out.println("[check] selected option: " + page.getSelectedText());

        Assert.assertEquals(page.getSelectedText(), DropdownPage.OPTION_1,
                "Option 1 must be selected after choosing it");
    }

    @Test(description = "The second option can be selected and becomes the selected one")
    public void shouldSelectSecondOption() {
        DropdownPage page = new DropdownPage(driver, wait);
        page.open();

        page.selectByVisibleText(DropdownPage.OPTION_2).waitUntilSelectedTextIs(DropdownPage.OPTION_2);
        System.out.println("[check] selected option: " + page.getSelectedText());

        Assert.assertEquals(page.getSelectedText(), DropdownPage.OPTION_2,
                "Option 2 must be selected after choosing it");
    }

    @Test(description = "Negative: the placeholder option is disabled and is not a real choice")
    public void shouldNotAllowSelectingDisabledPlaceholder() {
        DropdownPage page = new DropdownPage(driver, wait);
        page.open();

        Assert.assertTrue(page.isOptionDisabled(DropdownPage.PLACEHOLDER),
                "The placeholder option must be disabled");

        page.selectByVisibleText(DropdownPage.OPTION_1).waitUntilSelectedTextIs(DropdownPage.OPTION_1);
        System.out.println("[check] selected option after choosing a real one: " + page.getSelectedText());

        Assert.assertNotEquals(page.getSelectedText(), DropdownPage.PLACEHOLDER,
                "The disabled placeholder must not stay selected after a real option is chosen");
    }
}
