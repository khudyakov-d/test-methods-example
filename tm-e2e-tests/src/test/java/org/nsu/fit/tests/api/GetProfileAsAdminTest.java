package org.nsu.fit.tests.api;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.ContactPojo;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.services.rest.data.HealthCheckPojo;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Feature("Get admin profile")
public class GetProfileAsAdminTest {

    private static final RestClient restClient = new RestClient();

    private static AccountTokenPojo adminToken;

    @BeforeClass
    public static void setup() {
        adminToken = restClient.authenticate("admin", "setup");
    }

    @Test(description = "Get profile as admin")
    @Severity(SeverityLevel.BLOCKER)
    public void getProfileAsAdminTest() {
        ContactPojo adminProfile = restClient.get("/me", CustomerPojo.class, adminToken, null);

        assertEquals(adminProfile.login, "admin");
    }

}
