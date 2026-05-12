package project.mobile.pages.dashboard;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import project.mobile.base.BasePage;

public class DashboardPage extends BasePage {

    @AndroidFindBy(accessibility = "profileIcon")
    @iOSXCUITFindBy(accessibility = "profileIcon")
    private WebElement profileIcon;

    public DashboardPage() {
        super();
        if (!isElementLoaded(profileIcon)) {
            throw new AssertionError("Unable to load Dashboard page");
        }
    }
}
