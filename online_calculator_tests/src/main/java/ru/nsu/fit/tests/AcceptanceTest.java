package ru.nsu.fit.tests;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.nsu.fit.services.browser.Browser;
import ru.nsu.fit.services.browser.BrowserService;
import ru.nsu.fit.shared.AllureUtils;
import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.model.SeverityLevel;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
@Title("Acceptance test")
public class AcceptanceTest {
    private static final String PAGE_URL = "http://testmethods.tmweb.ru/";

    private static final By inputElement = By.xpath("//input[@type='text' and @name='Input']");
    private static final By equalElement = By.xpath("//input[@type='button' and @name='DoIt']");

    @Test
    @Title("Open page")
    @Description("Check that we can open page with Online Calculator")
    @Severity(SeverityLevel.BLOCKER)
    @Features("UI feature")
    public void openPage() {
        try (Browser browser = BrowserService.openNewBrowser()) {
            AllureUtils.saveTextLog("Open page");
            browser.openPage(PAGE_URL);
            AllureUtils.saveTextLog("The page was opened successfully");
            AllureUtils.saveImageAttach("Main screen", browser.makeScreenshot());
        }
    }

    @Test(dependsOnMethods = "openPage")
    @Title("Calculate some expression")
    @Description("Check that Online Calculator right calculates some expression (input case - keyboard)")
    @Severity(SeverityLevel.CRITICAL)
    @Features({"UI feature", "Subtraction", "Addition", "Multiplication", "Division"})
    public void calculateExpression() {
        try (Browser browser = BrowserService.openNewBrowser()) {
            browser.openPage(PAGE_URL);
            AllureUtils.saveImageAttach("Main screen", browser.makeScreenshot());
            browser.typeText(inputElement, "(1 + 2) * 5 - 10 / 2 + 0.3 * 2");
            AllureUtils.saveImageAttach("The expression is typed", browser.makeScreenshot());
            browser.click(equalElement);
            AllureUtils.saveImageAttach("Result", browser.makeScreenshot());
            Assert.assertEquals(browser.getValue(inputElement), "10.6");
        }
    }

    @Test(dependsOnMethods = "calculateExpression")
    @Title("Calculate some float expression")
    @Description("Check that Online Calculator right calculates some float expression (input case - keyboard)")
    @Severity(SeverityLevel.CRITICAL)
    @Features({"UI feature", "Addition"})
    public void calculateFloatExpression() {
        try (Browser browser = BrowserService.openNewBrowser()) {
            browser.openPage(PAGE_URL);
            AllureUtils.saveImageAttach("Main screen", browser.makeScreenshot());
            browser.typeText(inputElement, "-0.51984 + 0.2389");
            AllureUtils.saveImageAttach("The expression is typed", browser.makeScreenshot());
            browser.click(equalElement);
            AllureUtils.saveImageAttach("Result", browser.makeScreenshot());
            Assert.assertEquals(browser.getValue(inputElement), "-0.28094");
        }
    }
}
