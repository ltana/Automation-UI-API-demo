package project.flows;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;

import static project.plugins.InitializePlugin.*;

public class CapabilityBuilder {

    public static DesiredCapabilities buildCommonCapabilities() {
        DesiredCapabilities caps = new DesiredCapabilities();
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
        caps.setCapability("appium:autoGrantPermissions", "true");
        caps.setCapability("appium:real_mobile", "true");
        return caps;
    }

    public static void addBrowserStackOptions(DesiredCapabilities caps, String userName, String accessKey) {
        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("userName", userName);
        bstackOptions.put("accessKey", accessKey);
        bstackOptions.put("appiumVersion", "2.0.1");
        caps.setCapability("bstack:options", bstackOptions);
    }
}
