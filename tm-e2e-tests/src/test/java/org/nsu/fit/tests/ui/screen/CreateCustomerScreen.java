package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

public class CreateCustomerScreen extends Screen {

    private static final String CUSTOMER_FIRST_NAME = "//input[@name = 'firstName']";
    private static final String CUSTOMER_LAST_NAME = "//input[@name = 'lastName']";
    private static final String CUSTOMER_PASSWORD = "//input[@name = 'password']";
    private static final String CUSTOMER_LOGIN = "//input[@name = 'login']";

    private static final String CUSTOMER_CREATE_BUTTON = "//button[@type='submit' and text()='Create']";

    public static final String ERROR_MESSAGE = "//div[@role='alert']";

    public CreateCustomerScreen(Browser browser) {
        super(browser);
        browser.waitForElement(By.xpath(CUSTOMER_FIRST_NAME));
    }

    public CreateCustomerScreen fillFirstName(String firstName) {
        browser.typeText(By.xpath(CUSTOMER_FIRST_NAME), firstName);
        return this;
    }

    public CreateCustomerScreen fillLastName(String lastName) {
        browser.typeText(By.xpath(CUSTOMER_LAST_NAME), lastName);
        return this;
    }
    public CreateCustomerScreen fillPassword(String password) {
        browser.typeText(By.xpath(CUSTOMER_PASSWORD), password);
        return this;
    }

    public CreateCustomerScreen fillEmail(String email) {
        browser.typeText(By.xpath(CUSTOMER_LOGIN), email);
        return this;
    }

    // Лабораторная 4: Подумайте как обработать ситуацию,
    // когда при нажатии на кнопку Submit ('Create') не произойдет переход на AdminScreen,
    // а будет показана та или иная ошибка на текущем скрине.
    public AdminScreen clickSubmit() {
        browser.click(By.xpath(CUSTOMER_CREATE_BUTTON));

        if (browser.isElementPresent(By.xpath(ERROR_MESSAGE))) {
            throw new IllegalStateException("Error occurred during customer creation");
        }

        return new AdminScreen(browser);
    }

}
