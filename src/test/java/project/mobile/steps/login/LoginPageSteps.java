package project.mobile.steps.login;

import io.cucumber.java.en.Given;
import project.mobile.base.BasePage;

public class LoginPageSteps extends BasePage {

    @Given("User sets credentials as {string} for userID and {string} for password")
    public void userSetsUserIDAndPass(String userId, String password) {
        getPages().loginPage().loginWithUserAndPassword(userId, password).clickLoginButton();
    }
}
