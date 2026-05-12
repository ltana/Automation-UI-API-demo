package project.flows;

import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import project.api.APIUtils;

import java.util.*;

import static project.mobile.DriverInstance.quitDriver;
import static project.plugins.InitializePlugin.*;

public class CIRunFlow extends RunFlow {

    @Override
    public void getAPIBaseUrl() {
        APIUtils.baseUrlAccountsAPI = System.getenv("APIBaseUrlAccounts");
    }

    @Override
    public void createDriver()  {
        System.getProperties().put("http.proxyHost", "host");
        System.getProperties().put("http.proxyPort", "port");

        System.getProperties().put("https.proxyHost", "host");
        System.getProperties().put("https.proxyPort", "port");

        String userName = System.getenv("BROWSERSTACK_USERNAME");
        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");

        DesiredCapabilities caps = new DesiredCapabilities();
        final List<String> processArgs = new ArrayList<>(Arrays.asList("-automation-test", "true"));
        JSONObject argsValue = new JSONObject();
        argsValue.put("args", processArgs);

        String app = System.getenv("app");

        caps.setCapability("appium:buildName", "Build" + System.getenv("build"));
        caps.setCapability("appium:sessionName", "Session" + System.getenv("session"));
        caps.setCapability("appium:deviceName", deviceName.get());
        caps.setCapability("appium:platformVersion", osVersion.get());
        caps.setCapability("appium:language", deviceLanguage.get());
        caps.setCapability("appium:locale", deviceLocale.get());
        caps.setCapability("appium:fullReset", true);
        caps.setCapability("appium:name", testName.get());
        caps.setCapability("appium:browserstack.networkLogs", "true");
        caps.setCapability("appium:allowInvisibleElements", "true");
        caps.setCapability("appium:interactiveDebugging", "true");
        caps.setCapability("appium:customSnapshotTimeout", "1000");
        caps.setCapability("appium:webviewConnectTimeout", "5000");
        caps.setCapability("appium:autoGrantPermissions","true");
        caps.setCapability("appium:real_mobile", "true");

        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("userName", userName);
        bstackOptions.put("accessKey", accessKey);
        bstackOptions.put("appiumVersion", "2.0.1");
        caps.setCapability("bstack:options", bstackOptions);

        platform.createDriverForPlatform(caps, userName, accessKey, app);
    }

    @Override
    public void afterScenario() {
        quitDriver();
    }
}
