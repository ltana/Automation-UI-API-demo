package project.mobile.pages.welcome;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import project.mobile.base.BasePage;

public class AccessSendNotificationsPage extends BasePage {

    @AndroidFindBy(xpath = ".//headerAllowNotifications")
    @iOSXCUITFindBy(accessibility = "headerAllowNotifications")
    private WebElement headerAllowNotifications;

    @AndroidFindBy(xpath = ".//buttonAllow")
    @iOSXCUITFindBy(accessibility = "buttonAllow")
    private WebElement buttonAllow;

    public AccessSendNotificationsPage() {
        super();
        if (!isElementLoaded(headerAllowNotifications)) {
            throw new AssertionError("Unable to load Access Send NotificationsPage Page");
        }
    }

    public void clickButtonAllow() {
        buttonAllow.click();
    }
}
