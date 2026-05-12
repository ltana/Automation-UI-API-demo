package project.mobile.steps.welcome;

import io.cucumber.java.en.And;
import project.mobile.base.BasePage;

public class TermsAndConditionsSteps extends BasePage {

    @And("User accepts terms and conditions before login")
    public void userAcceptsTermsAndConditionsBeforeLogin() {
        getPages().termsAndConditionsPage().clickButtonContinue();
    }
}
