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
import static org.testng.Assert.assertThrows;

@Feature(value = "Buy plan")
public class BuyPlanWithNoMoneyTest extends BaseTest {

    private String email;
    private String password;

    private String planName;

    private LoginScreen loginScreen;
    private CustomerScreen customerScreen;

    @Test(description = "Login as admin and create customer")
    @Severity(SeverityLevel.BLOCKER)
    public void createCustomerAndPlan() {
        email = UUID.randomUUID() + "@gmail.com";
        password = "strongpass";

        planName = "plan-" + UUID.randomUUID();

        AdminScreen adminScreen = prepareCustomerCreation(email, password).clickSubmit();

        loginScreen = adminScreen.toCreatePlanScreen()
                .fillDetails("details")
                .fillFee(1000)
                .fillName(planName)
                .clickSubmit()
                .clickLogout();
    }

    @Test(description = "Login as a customer", dependsOnMethods = "createCustomerAndPlan")
    @Severity(SeverityLevel.BLOCKER)
    public void loginAsCustomer() {
        customerScreen = loginScreen.loginAsCustomer(email, password);
    }

    @Test(description = "Buy plan without money", dependsOnMethods = "loginAsCustomer")
    @Severity(SeverityLevel.BLOCKER)
    public void buyPlanWithoutMoney() {
        customerScreen = customerScreen.buyPlan(planName);
    }

    @Test(description = "Buy plan without balance top up", dependsOnMethods = "buyPlanWithoutMoney")
    @Severity(SeverityLevel.BLOCKER)
    public void checkThatPlanNotInSubscriptions() {
        assertThrows(() -> customerScreen.findPlanInSubscriptions(planName));
    }

}
