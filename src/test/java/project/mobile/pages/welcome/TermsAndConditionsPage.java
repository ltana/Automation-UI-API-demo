package project.mobile.pages.welcome;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import project.mobile.base.BasePage;

public class TermsAndConditionsPage extends BasePage {

    @AndroidFindBy(accessibility = "header")
    @iOSXCUITFindBy(accessibility = "header")
    private WebElement header;

    @AndroidFindBy(accessibility = "buttonContinue")
    @iOSXCUITFindBy(accessibility = "buttonContinue")
    private WebElement buttonContinue;

    public TermsAndConditionsPage() {
        super();
        if (!isElementLoaded(buttonContinue)) {
            throw new AssertionError("Unable to load Terms And Conditions Page");
        }
    }

    public void clickButtonContinue() {
        buttonContinue.click();
    }
}
