package project.mobile.pages.login;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.*;
import project.mobile.base.BasePage;

import static project.plugins.InitializePlugin.platform;

public class LoginPage extends BasePage {

    @AndroidFindBy(accessibility = "inputUsername")
    @iOSXCUITFindBy(accessibility = "inputUsername")
    private WebElement inputUsername;

    @AndroidFindBy(accessibility = "inputPassword")
    @iOSXCUITFindBy(accessibility = "inputPassword")
    private WebElement inputPassword;

    @AndroidFindBy(accessibility = "loginButton")
    @iOSXCUITFindBy(accessibility = "loginButton")
    private WebElement loginButton;

    public LoginPage() {
        super();
        if (!isElementLoaded(inputUsername)) {
            throw new AssertionError("Unable to load Login Page");
        }
    }

    public LoginPage loginWithUserAndPassword(String login, String pass) {
        inputUsername.clear();
        inputUsername.sendKeys(login);
        inputPassword.clear();
        inputPassword.sendKeys(pass);
        platform.hideKeyboard();
        return this;
    }

    public void clickLoginButton() {
        loginButton.click();
    }
}
