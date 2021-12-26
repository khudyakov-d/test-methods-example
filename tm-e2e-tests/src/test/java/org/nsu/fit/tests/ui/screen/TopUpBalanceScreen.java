package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

public class TopUpBalanceScreen extends Screen {

    private static final String TOP_UP_BALANCE_MONEY_INPUT = "//input[@name='money']";
    private static final String TOP_UP_BALANCE_SUBMIT = "//button[@type='submit' and text()='Submit']";
    private static final String TOP_UP_BALANCE_BACK = "//button[@type='button' and text()='Back']";
    private static final String TOP_UP_BALANCE_ERROR = "//div[@role='alert']";

    public TopUpBalanceScreen(Browser browser) {
        super(browser);
    }

    @Override
    protected void waitLoading() {
        browser.waitForElement(By.xpath(TOP_UP_BALANCE_MONEY_INPUT));
    }


    public TopUpBalanceScreen topUpBalance(Integer money) {
        browser.typeText(By.xpath(TOP_UP_BALANCE_MONEY_INPUT), String.valueOf(money));
        return this;
    }

    public CustomerScreen clickSubmit() {
        browser.click(By.xpath(TOP_UP_BALANCE_SUBMIT));

        if (browser.isElementPresent(By.xpath(TOP_UP_BALANCE_ERROR))) {
            throw new IllegalStateException("Error occurred during top up customer balance");
        }

        return new CustomerScreen(browser);
    }

    public CustomerScreen clickBack() {
        browser.click(By.xpath(TOP_UP_BALANCE_BACK));
        return new CustomerScreen(browser);
    }

}
