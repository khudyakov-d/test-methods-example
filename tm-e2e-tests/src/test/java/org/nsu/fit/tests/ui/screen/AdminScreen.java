package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

public class AdminScreen extends Screen {

    private static final String LOGOUT_HREF = "//a[.='Logout']";

    private static final String CUSTOMERS = "//h6[text() = 'Customers']";
    private static final String CUSTOMERS_ADD_BUTTON = "//button[@title='Add Customer']";
    private static final String CUSTOMERS_LAST_PAGE_BUTTON = "//h6[.='Customers']/../../..//span[@title='Last Page']";
    private static final String CUSTOMERS_TABLE_TD = "//h6[.='Customers']/../../..//tr/td[text() = '%s']";
    private static final String CUSTOMERS_SEARCH_INPUT = "//h6[.='Customers']/../..//input[@aria-label='Search']";

    private static final String PLANS_ADD_BUTTON = "//button[@title='Add plan']";
    private static final String PLANS_LAST_PAGE_BUTTON = "//h6[.='Plans']/../../..//span[@title='Last Page']";
    private static final String PLANS_TABLE_TD = "//h6[.='Plans']/../../..//tr/td[text() = '%s']";
    private static final String PLANS_SEARCH_INPUT = "//h6[.='Plans']/../..//input[@aria-label='Search']";


    public AdminScreen(Browser browser) {
        super(browser);
    }

    @Override
    protected void waitLoading() {
        browser.waitForElement(By.xpath(CUSTOMERS));
    }


    public LoginScreen clickLogout() {
        browser.click(By.xpath(LOGOUT_HREF));
        return new LoginScreen(browser);
    }

    public CreateCustomerScreen toCreateCustomerPage() {
        browser.click(By.xpath(CUSTOMERS_ADD_BUTTON));
        return new CreateCustomerScreen(browser);
    }

    public AdminScreen toLastCustomersPage() {
        browser.click(By.xpath(CUSTOMERS_LAST_PAGE_BUTTON));
        return this;
    }

    public boolean customerExistsInTable(String email) {
        return browser.isElementPresent(By.xpath(String.format(CUSTOMERS_TABLE_TD, email)));
    }

    public void searchCustomerByEmailAndWaitResult(String email) {
        browser.typeText(By.xpath(CUSTOMERS_SEARCH_INPUT), email);
        browser.waitForElement(By.xpath(String.format(CUSTOMERS_TABLE_TD, email)));
    }


    public CreatePlanScreen toCreatePlanScreen() {
        browser.click(By.xpath(PLANS_ADD_BUTTON));
        return new CreatePlanScreen(browser);
    }

    public AdminScreen toLastPlansPage() {
        browser.click(By.xpath(PLANS_LAST_PAGE_BUTTON));
        return this;
    }

    public boolean planExistsInTable(String email) {
        return browser.isElementPresent(By.xpath(String.format(PLANS_TABLE_TD, email)));
    }

    public void searchPlanByNameAndWaitResult(String name) {
        browser.typeText(By.xpath(PLANS_SEARCH_INPUT), name);
        browser.waitForElement(By.xpath(String.format(PLANS_TABLE_TD, name)));
    }

}
