package project.mobile.steps.welcome;

import io.cucumber.java.en.Given;
import project.mobile.base.BasePage;

public class AccessSendNotificationsSteps extends BasePage {

    @Given("User allows notifications")
    public void userAllowsNotifications() {
        getPages().accessSendNotificationsPage().clickButtonAllow();
    }

}
