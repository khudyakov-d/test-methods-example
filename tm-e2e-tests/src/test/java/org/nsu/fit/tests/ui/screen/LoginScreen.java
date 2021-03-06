package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

// Лабораторная 4: Необходимо имплементировать реализацию методов данного класса,
// а при необходимости расширить его.
public class LoginScreen extends Screen {

    public LoginScreen(Browser browser) {
        super(browser);
        browser.waitForElement(By.id("email"));
    }

    public AdminScreen loginAsAdmin() {
        browser.typeText(By.id("email"), "admin");
        browser.typeText(By.id("password"), "setup");

        browser.click(By.xpath("//button[@type = 'submit']"));

        // Лабораторная 4: В текущий момент в браузере еще не открылась
        // нужная страница (AdminScreen), и при обращении к ее элементам, могут происходить
        // те или иные ошибки. Подумайте как обработать эту ситуацию...
        return new AdminScreen(browser);
    }

}
