package org.example.herokuapp.tests;

import org.example.herokuapp.base.TestBase;
import org.example.herokuapp.pages.TyposPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the "Typos" page.
 *
 * The page introduces a typo randomly, so the text is checked against the allowed
 * variants instead of one hardcoded string. See {@link TyposPage} for the details.
 */
public class TyposTest extends TestBase {

    @Test(description = "The paragraph matches one of the two allowed text variants")
    public void shouldMatchOneOfAllowedParagraphVariants() {
        TyposPage page = new TyposPage(driver, wait);
        page.open();

        String actual = page.getFullText();
        System.out.println("[check] paragraph on this load: " + actual);

        Assert.assertTrue(TyposPage.ALLOWED_TEXTS.contains(actual),
                "The paragraph must be either the correct text or its known typo variant.\n"
                        + "Actual text: " + actual);
    }

    @Test(description = "The first sentence of the paragraph is always the same")
    public void shouldContainStableIntroduction() {
        TyposPage page = new TyposPage(driver, wait);
        page.open();

        Assert.assertEquals(page.getFirstParagraphText(), TyposPage.STABLE_PART,
                "The introduction sentence must not change between page loads");
    }
}
