package ru.nsu.fit.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.nsu.fit.services.browser.Browser;
import ru.nsu.fit.services.browser.BrowserService;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class AcceptanceTest {
    private static final String PAGE_URL = "http://testmethods.tmweb.ru/";

    private static final By inputElement = By.xpath("//input[@type='text' and @name='Input']");
    private static final By equalElement = By.xpath("//input[@type='button' and @name='DoIt']");

    @Test(description = "Check that we can open page with Online Calculator")
    public void openPage() {
        try (Browser browser = BrowserService.openNewBrowser()) {
            browser.openPage(PAGE_URL);
        }
    }

    @Test(description = "Check that Online Calculator right calculates some expression (input case - keyboard)", dependsOnMethods = "openPage")
    public void calculateExpression() {
        try (Browser browser = BrowserService.openNewBrowser()) {
            browser.openPage(PAGE_URL);
            browser.typeText(inputElement, "(1 + 2) * 5 - 10 / 2 + 0.3 * 2");
            browser.click(equalElement);
            Assert.assertEquals(browser.getValue(inputElement), "10.6");
        }
    }
}
