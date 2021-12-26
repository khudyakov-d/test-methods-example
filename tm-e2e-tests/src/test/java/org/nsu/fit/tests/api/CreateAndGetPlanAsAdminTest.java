package org.nsu.fit.tests.api;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.PlanListType;
import org.nsu.fit.services.rest.data.PlanPojo;
import org.nsu.fit.shared.JsonMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

@Feature("Create and get plan with admin token")
public class CreateAndGetPlanAsAdminTest {

    private static final String PLANS_URL = "/plans";

    private static final RestClient restClient = new RestClient();

    private static AccountTokenPojo adminToken;

    private String name;

    @BeforeClass
    public static void setup() {
        adminToken = restClient.authenticate("admin", "setup");
    }

    @Test(description = "Create not valid plan and expect error")
    @Severity(SeverityLevel.BLOCKER)
    public void createNotValidPlanTest() {
        PlanPojo planPojo = new PlanPojo();
        planPojo.name = "name";
        planPojo.details = "details";
        planPojo.fee = -1000;

        assertThrows(Exception.class,
                () -> restClient.post(PLANS_URL, JsonMapper.toJson(planPojo, true), Void.class, adminToken));
    }

    @Test(description = "Create plan")
    @Severity(SeverityLevel.BLOCKER)
    public void createValidPlanTest() {
        name = UUID.randomUUID().toString();

        PlanPojo planPojo = new PlanPojo();
        planPojo.name = name;
        planPojo.details = "details";
        planPojo.fee = 1000;

        restClient.post(PLANS_URL, JsonMapper.toJson(planPojo, true), Void.class, adminToken);
    }

    @Test(description = "Get created plan", dependsOnMethods = "createValidPlanTest")
    @Severity(SeverityLevel.BLOCKER)
    public void getPlanTest() {
        List<PlanPojo> plans = restClient.get(PLANS_URL, new PlanListType(), adminToken, new HashMap<>());
        assertTrue(plans.stream().anyMatch(p -> p.name.equals(name)));
    }

}
