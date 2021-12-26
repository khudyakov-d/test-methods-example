package org.nsu.fit.tests.ui.admin.plan;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.tests.ui.BaseTest;
import org.nsu.fit.tests.ui.screen.AdminScreen;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.AssertJUnit.assertTrue;

@Feature("Create plan")
public class CreatePlanTest extends BaseTest {

    private String planName;
    private AdminScreen adminScreen;

    @Test(description = "Create plan")
    @Severity(SeverityLevel.BLOCKER)
    public void createPlan() {
        planName = "plan-" + UUID.randomUUID();
        adminScreen = login()
                .toCreatePlanScreen()
                .fillDetails("details")
                .fillFee(1000)
                .fillName(planName)
                .clickSubmit();
    }

    @Test(description = "Check that plan exists in list after creation", dependsOnMethods = "createPlan")
    @Severity(SeverityLevel.BLOCKER)
    public void checkThatPlanExistsInTableAfterCreation() {
        boolean planExistsInTable = adminScreen
                .toLastPlansPage()
                .planExistsInTable(planName);

        assertTrue(planExistsInTable);
    }

}
