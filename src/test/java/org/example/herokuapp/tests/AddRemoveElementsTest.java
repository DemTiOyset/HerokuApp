package org.example.herokuapp.tests;

import org.example.herokuapp.base.TestBase;
import org.example.herokuapp.pages.AddRemoveElementsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the "Add/Remove Elements" page.
 *
 * Positive cases: adding elements and deleting one of them.
 * Negative case: deleting on an untouched page must not break anything.
 */
public class AddRemoveElementsTest extends TestBase {

    @Test(description = "Two clicks on Add Element produce two elements")
    public void shouldAddTwoElements() {
        AddRemoveElementsPage page = new AddRemoveElementsPage(driver, wait);
        page.open();

        Assert.assertEquals(page.getElementsCount(), 0, "A new page must not contain added elements");

        page.addElements(2).waitUntilElementsCountIs(2);
        System.out.println("[check] elements after two clicks: " + page.getElementsCount());

        Assert.assertEquals(page.getElementsCount(), 2, "Two clicks on Add Element must add two elements");
    }

    @Test(description = "Deleting one element of two leaves exactly one element")
    public void shouldDeleteOneOfTwoElements() {
        AddRemoveElementsPage page = new AddRemoveElementsPage(driver, wait);
        page.open();

        page.addElements(2).waitUntilElementsCountIs(2);
        page.deleteElement().waitUntilElementsCountIs(1);
        System.out.println("[check] elements after delete: " + page.getElementsCount());

        Assert.assertEquals(page.getElementsCount(), 1, "After deleting one element exactly one must remain");
    }

    @Test(description = "Negative: there is nothing to delete until Add Element is clicked")
    public void shouldNotDeleteAnythingOnUntouchedPage() {
        AddRemoveElementsPage page = new AddRemoveElementsPage(driver, wait);
        page.open();

        Assert.assertEquals(page.getDeleteButtonsCount(), 0,
                "The Delete button must not exist before an element is added");

        // There is no Delete button at all, so nothing can be deleted and the counter stays zero.
        Assert.assertEquals(page.getElementsCount(), 0, "An untouched page must stay empty");
        System.out.println("[check] delete buttons on an untouched page: " + page.getDeleteButtonsCount());
    }
}
