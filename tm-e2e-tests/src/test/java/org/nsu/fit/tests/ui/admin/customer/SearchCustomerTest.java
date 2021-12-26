package org.nsu.fit.tests.ui.admin.customer;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.tests.ui.BaseTest;
import org.nsu.fit.tests.ui.screen.AdminScreen;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.AssertJUnit.assertTrue;

@Feature("Search customer")
public class SearchCustomerTest extends BaseTest {

    private String email;
    private AdminScreen adminScreen;

    @Test(description = "Create customer")
    @Severity(SeverityLevel.BLOCKER)
    public void createCustomer() {
        email = UUID.randomUUID() + "@gmail.com";
        adminScreen = prepareCustomerCreation(email, "strongpass").clickSubmit();
    }

    @Test(description = "Search customer after creation via search line", dependsOnMethods = "createCustomer")
    @Severity(SeverityLevel.BLOCKER)
    public void checkCustomerExists() {
        adminScreen.searchCustomerByEmailAndWaitResult(email);
    }

}
