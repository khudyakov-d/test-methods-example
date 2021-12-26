package org.nsu.fit.tests.ui;

import com.github.javafaker.Faker;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;
import org.nsu.fit.tests.ui.screen.AdminScreen;
import org.nsu.fit.tests.ui.screen.CreateCustomerScreen;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class BaseTest {

    protected Browser browser = null;

    private static final Faker faker = new Faker();

    @BeforeClass
    public void setup() {
        browser = BrowserService.openNewBrowser();
    }

    protected AdminScreen login() {
        return new LoginScreen(browser).loginAsAdmin();
    }

    protected CreateCustomerScreen prepareCustomerCreation(String email, String password) {
        return new LoginScreen(browser)
                .loginAsAdmin()
                .toCreateCustomerPage()
                .fillFirstName(faker.name().firstName())
                .fillLastName(faker.name().lastName())
                .fillEmail(email)
                .fillPassword(password);
    }

    @AfterClass
    public void close() {
        if (browser != null) {
            browser.close();
        }
    }

}
