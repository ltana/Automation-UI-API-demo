package project.mobile.base;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import project.common.Context;
import project.common.YamlParser;

import java.time.Duration;

import static project.mobile.DriverInstance.getDriver;

public class BasePage {
    protected YamlParser yaml;
    public static ThreadLocal<Pages> pages = ThreadLocal.withInitial(Pages::new);

    public static Pages getPages() {
        return pages.get();
    }

    public BasePage() {
        loadPageElements();
    }

    private void loadPageElements() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
        yaml = Context.getYaml();
    }

    public boolean isElementLoaded(WebElement loadElement) {
        var pollingTimeout = 1;
        try {
            new FluentWait<WebDriver>(getDriver())
                .pollingEvery(Duration.ofSeconds(pollingTimeout))
                .ignoring(NoSuchElementException.class)
                .until(ExpectedConditions.visibilityOf(loadElement));
            return loadElement.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}
