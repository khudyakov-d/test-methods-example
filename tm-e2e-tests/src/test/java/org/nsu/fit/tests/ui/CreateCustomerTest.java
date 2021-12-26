package org.nsu.fit.tests.ui;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.tests.ui.screen.AdminScreen;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.AssertJUnit.assertTrue;

@Feature("Create customer")
public class CreateCustomerTest extends BaseTest {

    private String email;
    private AdminScreen adminScreen;

    @Test(description = "Create customer")
    @Severity(SeverityLevel.BLOCKER)
    public void createCustomer() {
        email = UUID.randomUUID() + "@gmail.com";
        adminScreen = prepareCustomerCreation(email, "strongpass").clickSubmit();
    }

    @Test(description = "Check that customer exists in list after creation", dependsOnMethods = "createCustomer")
    @Severity(SeverityLevel.BLOCKER)
    public void checkCustomerExists() {
        boolean customerExistsInTable = adminScreen
                .toLastCustomersPage()
                .customerExistsInTable(email);

        assertTrue(customerExistsInTable);
    }

}
