package org.nsu.fit.tests.api;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.ContactPojo;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Feature("Get customer profile")
public class GetProfileAsCustomerTest {

    private static final RestClient restClient = new RestClient();

    private static AccountTokenPojo adminToken;

    @BeforeClass
    public static void setup() {
        adminToken = restClient.authenticate("admin", "setup");
    }

    @Test(description = "Get profile as customer")
    @Severity(SeverityLevel.BLOCKER)
    public void getProfileAsCustomerTest() {
        CustomerPojo customerPojo = restClient.createAutoGeneratedCustomer(adminToken);
        AccountTokenPojo customerToken = restClient.authenticate(customerPojo.login, customerPojo.pass);
        CustomerPojo customerProfile = restClient.get("/me", CustomerPojo.class, customerToken, null);

        assertNotNull(customerProfile.id);
        assertEquals(customerProfile.login, customerPojo.login);
        assertEquals(customerProfile.firstName, customerPojo.firstName);
        assertEquals(customerProfile.lastName, customerPojo.lastName);
        assertEquals(customerProfile.balance, 0);
    }

}