package project.mobile.steps.dashboard;

import io.cucumber.java.en.And;
import project.mobile.base.BasePage;

public class DashboardSteps extends BasePage {

    @And("The Dashboard page is displayed")
    public void dashboardPageIsDisplayed() {
        getPages().dashboardPage();
    }
}
