package org.nsu.fit.tests.ui.customer;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.tests.ui.BaseTest;
import org.nsu.fit.tests.ui.screen.AdminScreen;
import org.nsu.fit.tests.ui.screen.CustomerScreen;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.Assert.assertEquals;

@Feature(value = "Buy plan")
public class BuyPlanTest extends BaseTest {

    private String email;
    private String password;

    private String planName;
    private int planFee;
    private int customerBalance;

    private LoginScreen loginScreen;
    private CustomerScreen customerScreen;

    @Test(description = "Login as admin and create customer")
    @Severity(SeverityLevel.BLOCKER)
    public void createCustomerAndPlan() {
        email = UUID.randomUUID() + "@gmail.com";
        password = "strongpass";

        planName = "plan-" + UUID.randomUUID();
        planFee = 1000;

        AdminScreen adminScreen = prepareCustomerCreation(email, password).clickSubmit();

        loginScreen = adminScreen.toCreatePlanScreen()
                .fillDetails("details")
                .fillFee(planFee)
                .fillName(planName)
                .clickSubmit()
                .clickLogout();
    }

    @Test(description = "Login as a customer", dependsOnMethods = "createCustomerAndPlan")
    @Severity(SeverityLevel.BLOCKER)
    public void loginAsCustomer() {
        customerScreen = loginScreen.loginAsCustomer(email, password);
    }

    @Test(description = "Top up balance and check that balance was updated", dependsOnMethods = "loginAsCustomer")
    @Severity(SeverityLevel.BLOCKER)
    public void topUpBalance() {
        customerBalance = customerScreen
                .toTopUpBalancePage()
                .topUpBalance(planFee)
                .clickSubmit()
                .getBalance();
    }

    @Test(description = "Buy plan and find him in subscriptions", dependsOnMethods = "topUpBalance")
    @Severity(SeverityLevel.BLOCKER)
    public void buyPlanAndSearchInSubscriptions() {
        customerScreen
                .buyPlan(planName)
                .findPlanInSubscriptions(planName);
    }

    @Test(description = "Check that customer balance decreased by plan fee",
            dependsOnMethods = "buyPlanAndSearchInSubscriptions")
    @Severity(SeverityLevel.BLOCKER)
    public void checkThatCustomerBalanceUpdated() {
        int resultBalance = customerScreen.getBalance();
        assertEquals(resultBalance, customerBalance - planFee);
    }

}
