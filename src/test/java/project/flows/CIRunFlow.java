package project.flows;

import org.openqa.selenium.remote.DesiredCapabilities;
import project.api.APIUtils;

import static project.mobile.DriverInstance.quitDriver;
import static project.plugins.InitializePlugin.*;

public class CIRunFlow implements RunFlow {

    @Override
    public void getAPIBaseUrl() {
        APIUtils.setBaseUrlAccountsAPI(System.getenv("APIBaseUrlAccounts"));
    }

    @Override
    public void createDriver() {
        System.getProperties().put("http.proxyHost", "host");
        System.getProperties().put("http.proxyPort", "port");
        System.getProperties().put("https.proxyHost", "host");
        System.getProperties().put("https.proxyPort", "port");

        String userName = System.getenv("BROWSERSTACK_USERNAME");
        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
        String app = System.getenv("app");

        DesiredCapabilities caps = CapabilityBuilder.buildCommonCapabilities();
        caps.setCapability("appium:buildName", "Build" + System.getenv("build"));
        caps.setCapability("appium:sessionName", "Session" + System.getenv("session"));
        CapabilityBuilder.addBrowserStackOptions(caps, userName, accessKey);

        platform.createDriverForPlatform(caps, userName, accessKey, app);
    }

    @Override
    public void afterScenario() {
        quitDriver();
    }
}
