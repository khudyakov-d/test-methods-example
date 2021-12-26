package org.nsu.fit.tests.ui.customer;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.tests.ui.BaseTest;
import org.nsu.fit.tests.ui.screen.CustomerScreen;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.nsu.fit.tests.ui.screen.TopUpBalanceScreen;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

@Feature(value = "Top up balance as customer")
public class TopUpBalanceNegativeSumTest extends BaseTest {

    private String email;
    private String password;

    private LoginScreen loginScreen;
    private CustomerScreen customerScreen;

    @Test(description = "Login as admin and create customer")
    @Severity(SeverityLevel.BLOCKER)
    public void createCustomer() {
        email = UUID.randomUUID() + "@gmail.com";
        password = "strongpass";

        loginScreen = prepareCustomerCreation(email, password)
                .clickSubmit()
                .clickLogout();
    }

    @Test(description = "Login as a customer", dependsOnMethods = "createCustomer")
    @Severity(SeverityLevel.BLOCKER)
    public void loginAsCustomer() {
        customerScreen = loginScreen.loginAsCustomer(email, password);
    }

    @Test(description = "Top up balance with negative sum and expect error", dependsOnMethods = "loginAsCustomer")
    @Severity(SeverityLevel.BLOCKER)
    public void topUpBalanceWithNegativeSum() {
        int sum = -1000;
        TopUpBalanceScreen topUpBalanceScreen = this.customerScreen
                .toTopUpBalancePage()
                .topUpBalance(sum);

        assertThrows(IllegalStateException.class, topUpBalanceScreen::clickSubmit);
    }


}
