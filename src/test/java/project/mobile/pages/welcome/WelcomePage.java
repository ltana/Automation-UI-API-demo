package project.mobile.pages.welcome;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import project.mobile.base.BasePage;

public class WelcomePage extends BasePage {

    @AndroidFindBy(accessibility = "headerWelcome")
    @iOSXCUITFindBy(className = "headerWelcome")
    private WebElement headerWelcome;

    @AndroidFindBy(accessibility = "buttonIAmAlreadyACustomer")
    @iOSXCUITFindBy(accessibility = "buttonIAmAlreadyACustomer")
    private WebElement buttonIAmAlreadyACustomer;

    public WelcomePage() {
        super();
        if (!isElementLoaded(headerWelcome)) {
            throw new AssertionError("Unable to load Welcome Page");
        }
    }

    public void clickButtonIAmAlreadyACustomer() {
        buttonIAmAlreadyACustomer.click();
    }
}
