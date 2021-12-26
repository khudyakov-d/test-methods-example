package org.nsu.fit.tests.ui.admin.plan;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.tests.ui.BaseTest;
import org.nsu.fit.tests.ui.screen.AdminScreen;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

@Feature("Create plan")
public class CreatePlanCancelTest extends BaseTest {

    private String planName;
    private AdminScreen adminScreen;

    @Test(description = "Fill plan form and click cancel")
    @Severity(SeverityLevel.BLOCKER)
    public void createPlan() {
        planName = "plan-" + UUID.randomUUID();
        adminScreen = login()
                .toCreatePlanScreen()
                .fillDetails("details")
                .fillFee(1000)
                .fillName(planName)
                .clickCancel();
    }

    @Test(description = "Check that plan doesn't exist in list after creation", dependsOnMethods = "createPlan")
    @Severity(SeverityLevel.BLOCKER)
    public void checkThatPlanExistsInTableAfterCreation() {
        boolean planExistsInTable = adminScreen
                .toLastPlansPage()
                .planExistsInTable(planName);

        assertFalse(planExistsInTable);
    }

}
