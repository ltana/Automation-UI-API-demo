package project.mobile.steps.welcome;

import io.cucumber.java.en.And;
import project.mobile.base.BasePage;

public class WelcomeSteps extends BasePage {

    @And("User selects login as an existing customer")
    public void userSelectsLoginAsAnExistingCustomer() {
        getPages().welcomePage().clickButtonIAmAlreadyACustomer();
    }

}
