package org.nsu.fit.tests.ui.admin.plan;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.tests.ui.BaseTest;
import org.nsu.fit.tests.ui.screen.AdminScreen;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.UUID;

import static org.testng.AssertJUnit.assertTrue;

@Feature("Search plan")
public class SearchPlanTest extends BaseTest {

    private static final Random random = new Random();

    private String planName;

    private AdminScreen adminScreen;

    @Test(description = "Create plan")
    @Severity(SeverityLevel.BLOCKER)
    public void createPlan() {
        planName = "plan-" + UUID.randomUUID();
        adminScreen = login()
                .toCreatePlanScreen()
                .fillDetails("details")
                .fillFee(1000 + random.nextInt(1000))
                .fillName(planName)
                .clickSubmit();
    }

    @Test(description = "Search plan after creation via search line by name",
            dependsOnMethods = "createPlan")
    @Severity(SeverityLevel.BLOCKER)
    public void checkPlanWithNameExists() {
        adminScreen.searchPlanByNameAndWaitResult(planName);
    }

}
