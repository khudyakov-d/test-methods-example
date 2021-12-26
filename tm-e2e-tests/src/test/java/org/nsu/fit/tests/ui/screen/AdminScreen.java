package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

public class AdminScreen extends Screen {

    private static final String CUSTOMERS = "//h6[text() = 'Customers']";
    private static final String CUSTOMERS_ADD_BUTTON = "//button[@title='Add Customer']";
    private static final String CUSTOMERS_LAST_PAGE_BUTTON = "//h6[.='Customers']/../../..//span[@title='Last Page']";
    private static final String CUSTOMERS_TABLE_TD = "//h6[.='Customers']/../../..//tr/td[text() = '%s']";

    public AdminScreen(Browser browser) {
        super(browser);
        browser.waitForElement(By.xpath(CUSTOMERS));
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

}
