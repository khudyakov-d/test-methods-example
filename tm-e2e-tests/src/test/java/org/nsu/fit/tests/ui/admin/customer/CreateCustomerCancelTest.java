package org.nsu.fit.tests.ui.admin.customer;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.tests.ui.BaseTest;
import org.nsu.fit.tests.ui.screen.AdminScreen;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

@Feature("Create customer")
public class CreateCustomerCancelTest extends BaseTest {

    private String email;
    private AdminScreen adminScreen;

    @Test(description = "Fill customer form and click cancel")
    @Severity(SeverityLevel.BLOCKER)
    public void createCustomer() {
        email = UUID.randomUUID() + "@gmail.com";
        adminScreen = prepareCustomerCreation(email, "strongpass").clickCancel();
    }

    @Test(description = "Check that customer doesn't exist in list after creation", dependsOnMethods = "createCustomer")
    @Severity(SeverityLevel.BLOCKER)
    public void checkCustomerExists() {
        boolean customerExistsInTable = adminScreen
                .toLastCustomersPage()
                .customerExistsInTable(email);

        assertFalse(customerExistsInTable);
    }

}
