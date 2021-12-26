package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerScreen extends Screen {

    private static final Pattern BALANCE_PATTERN = Pattern.compile("Your balance: (?<balance>[0-9]*)");

    private static final String SUBSCRIPTIONS = "//h6[text() = 'Subscriptions']";
    private static final String SUBSCRIPTIONS_TABLE_TD = "//h6[. = 'Subscriptions']/../../..//tr/td[text() = '%s']";
    private static final String SUBSCRIPTIONS_SEARCH_INPUT = "//h6[.='Subscriptions']/../..//input[@aria-label='Search']";

    private static final String CUSTOMER_TOP_UP_BALANCE = "//a[text()='Top up balance']";
    private static final String CUSTOMER_BALANCE = "//h3[contains(text(), 'Your balance')]";

    private static final String BUY_PLAN_BUTTON = "//h6[.='Plans']/../../..//tr/td[text() = '%s']/..//button";


    public CustomerScreen(Browser browser) {
        super(browser);
    }

    @Override
    protected void waitLoading() {
        browser.waitForElement(By.xpath(SUBSCRIPTIONS));
    }

    public TopUpBalanceScreen toTopUpBalancePage() {
        browser.click(By.xpath(CUSTOMER_TOP_UP_BALANCE));
        return new TopUpBalanceScreen(browser);
    }

    public int getBalance() {
        String value = browser.getText(By.xpath(CUSTOMER_BALANCE));
        Matcher matcher = BALANCE_PATTERN.matcher(value);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group("balance"));
        }

        throw new IllegalStateException("Couldn't get balance value");
    }

    public CustomerScreen buyPlan(String planName) {
        browser.click(By.xpath(String.format(BUY_PLAN_BUTTON, planName)));
        return this;
    }

    public void findPlanInSubscriptions(String planName) {
        browser.typeText(By.xpath(SUBSCRIPTIONS_SEARCH_INPUT), planName);
        browser.waitForElement(By.xpath(String.format(SUBSCRIPTIONS_TABLE_TD, planName)));
    }

}
