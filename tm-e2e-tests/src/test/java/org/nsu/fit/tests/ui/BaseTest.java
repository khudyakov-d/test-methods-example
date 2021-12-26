package org.nsu.fit.tests.ui;

import com.github.javafaker.Faker;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class BaseTest {

    protected Browser browser = null;

    private static final Faker faker = new Faker();

    @BeforeClass
    public void setup() {
        browser = BrowserService.openNewBrowser();
    }

    @AfterClass
    public void close() {
        if (browser != null) {
            browser.close();
        }
    }

}
