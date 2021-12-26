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

import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

@Feature("Delete plan with admin token")
public class DeletePlanAsAdminTest {

    private static final String PLANS_URL = "/plans";

    private static final RestClient restClient = new RestClient();

    private static AccountTokenPojo adminToken;

    private PlanPojo plan;

    @BeforeClass
    public static void setup() {
        adminToken = restClient.authenticate("admin", "setup");
    }

    @Test(description = "Create plan")
    @Severity(SeverityLevel.BLOCKER)
    public void createPlanTest() {
        PlanPojo newPlan = new PlanPojo();
        newPlan.name = UUID.randomUUID().toString();
        newPlan.details = "details";
        newPlan.fee = 1000;

        plan = restClient.post(PLANS_URL, JsonMapper.toJson(newPlan, true), PlanPojo.class, adminToken);
    }

    @Test(description = "Delete created plan", dependsOnMethods = "createPlanTest")
    @Severity(SeverityLevel.BLOCKER)
    public void deletePlanTest() {
        String path = PLANS_URL + "/" + plan.id;
        restClient.delete(path, adminToken);
    }

    @Test(description = "Check that the plan does not exist", dependsOnMethods = "deletePlanTest")
    @Severity(SeverityLevel.BLOCKER)
    public void getPlans() {
        List<PlanPojo> plans = restClient.get(PLANS_URL, new PlanListType(), adminToken, new HashMap<>());
        assertTrue(plans.stream().noneMatch(p -> p.name.equals(plan.name)));
    }

}
