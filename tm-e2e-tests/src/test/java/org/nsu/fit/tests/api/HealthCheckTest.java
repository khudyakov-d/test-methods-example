package org.nsu.fit.tests.api;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.HealthCheckPojo;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Feature("Health check")
public class HealthCheckTest {

    private final RestClient restClient = new RestClient();

    @Test(description = "Health check test")
    @Severity(SeverityLevel.BLOCKER)
    public void healthCheckTest() {
        HealthCheckPojo result = restClient.get("health_check", HealthCheckPojo.class, null, null);
        assertEquals(result.status, "OK");
        assertEquals(result.dbStatus, "OK");
    }

}
