package org.nsu.fit.tests.api;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.CustomerListType;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.InternalServerErrorException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertThrows;

@Feature("Delete customer with admin token")
public class DeleteCustomerAsAdminTest {

    private static final RestClient restClient = new RestClient();

    private static AccountTokenPojo adminToken;

    private String login;

    @BeforeClass
    public static void setup() {
        adminToken = restClient.authenticate("admin", "setup");
    }

    @Test(description = "Delete customer")
    @Severity(SeverityLevel.BLOCKER)
    public void deleteCustomerTest() {
        CustomerPojo customer = restClient.createAutoGeneratedCustomer(adminToken);
        AccountTokenPojo customerToken = restClient.authenticate(customer.login, customer.pass);
        login = customer.login;
        String path = "customers/" + customer.id;

        assertThrows(InternalServerErrorException.class, () -> restClient.delete(path, customerToken));
    }

    @Test(description = "Check that customer not exists after deletion",
            dependsOnMethods = "deleteCustomerTest")
    @Severity(SeverityLevel.BLOCKER)
    public void checkCustomerNotExistTest() {
        Map<String, List<Object>> queryParams = new HashMap<>();
        queryParams.put("login", Collections.singletonList(login));
        List<CustomerPojo> result = restClient.get("customers", new CustomerListType(), adminToken, queryParams);

        assertFalse(result.isEmpty());
    }

}