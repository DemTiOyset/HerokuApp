package org.example.herokuapp.tests;

import org.example.herokuapp.base.TestBase;
import org.example.herokuapp.pages.InputsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the "Inputs" page.
 *
 * Positive cases: digits are accepted, ARROW_UP and ARROW_DOWN change the value by one.
 * Negative case: letters are not a valid value for a number field.
 */
public class InputsTest extends TestBase {

    @Test(description = "A number can be typed and ARROW_UP increases it by one")
    public void shouldIncrementValueWithArrowUp() {
        InputsPage page = new InputsPage(driver, wait);
        page.open();

        page.typeValue("10").waitUntilValueIs("10");
        Assert.assertEquals(page.getValue(), "10", "The field must accept a typed number");

        page.pressArrowUp().waitUntilValueIs("11");
        System.out.println("[check] value after ARROW_UP: " + page.getValue());

        Assert.assertEquals(page.getValue(), "11", "ARROW_UP must increase the value by one");
    }

    @Test(description = "ARROW_DOWN decreases the value by one")
    public void shouldDecrementValueWithArrowDown() {
        InputsPage page = new InputsPage(driver, wait);
        page.open();

        page.typeValue("10").waitUntilValueIs("10");
        page.pressArrowDown().waitUntilValueIs("9");
        System.out.println("[check] value after ARROW_DOWN: " + page.getValue());

        Assert.assertEquals(page.getValue(), "9", "ARROW_DOWN must decrease the value by one");
    }

    @Test(description = "Negative: non-numeric text is not accepted by the number field")
    public void shouldRejectNonNumericInput() {
        InputsPage page = new InputsPage(driver, wait);
        page.open();

        page.typeValue("abc");
        System.out.println("[check] value after typing 'abc': '" + page.getValue() + "'");

        Assert.assertEquals(page.getValue(), "",
                "A number field must ignore letters and stay empty");
    }
}
