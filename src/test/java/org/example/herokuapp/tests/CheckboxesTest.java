package org.example.herokuapp.tests;

import org.example.herokuapp.base.TestBase;
import org.example.herokuapp.pages.CheckboxesPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the "Checkboxes" page.
 *
 * Positive cases: checking the first checkbox and unchecking the second one.
 * Negative case: toggling one checkbox must not change the other one.
 */
public class CheckboxesTest extends TestBase {

    private static final int FIRST = 0;
    private static final int SECOND = 1;

    @Test(description = "The first checkbox is unchecked initially and can be checked")
    public void shouldCheckFirstCheckbox() {
        CheckboxesPage page = new CheckboxesPage(driver, wait);
        page.open();

        Assert.assertEquals(page.getCheckboxesCount(), 2, "The page must contain two checkboxes");
        Assert.assertFalse(page.isChecked(FIRST), "The first checkbox must be unchecked after the page load");

        page.check(FIRST).waitUntilChecked(FIRST, true);
        System.out.println("[check] first checkbox is checked: " + page.isChecked(FIRST));

        Assert.assertTrue(page.isChecked(FIRST), "The first checkbox must become checked after the click");
    }

    @Test(description = "The second checkbox is checked initially and can be unchecked")
    public void shouldUncheckSecondCheckbox() {
        CheckboxesPage page = new CheckboxesPage(driver, wait);
        page.open();

        Assert.assertTrue(page.isChecked(SECOND), "The second checkbox must be checked after the page load");

        page.uncheck(SECOND).waitUntilChecked(SECOND, false);
        System.out.println("[check] second checkbox is checked: " + page.isChecked(SECOND));

        Assert.assertFalse(page.isChecked(SECOND), "The second checkbox must become unchecked after the click");
    }

    @Test(description = "Negative: toggling the first checkbox does not affect the second one")
    public void shouldNotAffectSecondCheckboxWhenFirstIsToggled() {
        CheckboxesPage page = new CheckboxesPage(driver, wait);
        page.open();

        boolean secondBefore = page.isChecked(SECOND);
        page.check(FIRST).waitUntilChecked(FIRST, true);
        boolean secondAfter = page.isChecked(SECOND);
        System.out.println("[check] second checkbox before/after: " + secondBefore + "/" + secondAfter);

        Assert.assertEquals(secondAfter, secondBefore,
                "The state of the second checkbox must not depend on the first one");
    }
}
