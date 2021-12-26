package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

public class CreatePlanScreen extends Screen {

    private static final String PLAN_NAME = "//input[@name = 'name']";
    private static final String PLAN_DETAILS = "//input[@name = 'details']";
    private static final String PLAN_FEE = "//input[@name = 'fee']";
    private static final String PLAN_CREATE_BUTTON = "//button[@type='submit' and text()='Create']";
    private static final String PLAN_BACK_BUTTON = "//button[@type='button' and text()='Back']";

    private static final String PLAN_CREATION_ERROR = "//div[@role='alert']";

    public CreatePlanScreen(Browser browser) {
        super(browser);
    }

    @Override
    protected void waitLoading() {
        browser.waitForElement(By.xpath(PLAN_NAME));
    }

    public CreatePlanScreen fillName(String name) {
        browser.typeText(By.xpath(PLAN_NAME), name);
        return this;
    }

    public CreatePlanScreen fillDetails(String details) {
        browser.typeText(By.xpath(PLAN_DETAILS), details);
        return this;
    }
    public CreatePlanScreen fillFee(Integer fee) {
        browser.typeText(By.xpath(PLAN_FEE), String.valueOf(fee));
        return this;
    }

    public AdminScreen clickSubmit() {
        browser.click(By.xpath(PLAN_CREATE_BUTTON));

        if (browser.isElementPresent(By.xpath(PLAN_CREATION_ERROR))) {
            throw new IllegalStateException("Error occurred during plan creation");
        }

        return new AdminScreen(browser);
    }

    public AdminScreen clickCancel() {
        browser.click(By.xpath(PLAN_BACK_BUTTON));
        return new AdminScreen(browser);
    }
}
