package org.example.herokuapp.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object for https://the-internet.herokuapp.com/typos
 *
 * The page intentionally introduces a typo on every load. Verified on the live site
 * (40 loads): only one character changes, the apostrophe in the last word becomes a comma:
 *
 *   "... other times you won't."   - correct text
 *   "... other times you won,t."   - text with the typo
 *
 * Everything else stays the same, so the test checks the paragraph against these two
 * allowed variants instead of comparing it with a single hardcoded string.
 */
public class TyposPage extends BasePage {

    private static final By PARAGRAPHS = By.tagName("p");

    public static final String STABLE_PART =
            "This example demonstrates a typo being introduced. It does it randomly on each page load.";

    public static final List<String> ALLOWED_TEXTS = List.of(
            STABLE_PART + " Sometimes you'll see a typo, other times you won't.",
            STABLE_PART + " Sometimes you'll see a typo, other times you won,t.");

    public TyposPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    protected String getPath() {
        return "/typos";
    }

    @Override
    protected void waitForPageLoaded() {
        wait.until(ExpectedConditions.presenceOfElementLocated(PARAGRAPHS));
    }

    /** Texts of all the paragraphs of the page, in the render order. */
    public List<String> getParagraphTexts() {
        wait.until(ExpectedConditions.presenceOfElementLocated(PARAGRAPHS));
        return driver.findElements(PARAGRAPHS).stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(text -> !text.isEmpty())
                .collect(Collectors.toList());
    }

    /** The whole readable text of the example, joined into a single line. */
    public String getFullText() {
        return String.join(" ", getParagraphTexts());
    }

    public String getFirstParagraphText() {
        return getParagraphTexts().get(0);
    }

    /** True when the paragraph matches one of the allowed variants. */
    public boolean hasAllowedText() {
        return ALLOWED_TEXTS.contains(getFullText());
    }
}
