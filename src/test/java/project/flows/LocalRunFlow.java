package project.flows;

import com.browserstack.local.Local;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import project.api.APIUtils;
import project.common.YamlParser;

import java.util.*;

import static project.mobile.DriverInstance.quitDriver;
import static project.plugins.InitializePlugin.*;

public class LocalRunFlow extends RunFlow {

    @Override
    public void getAPIBaseUrl() {
        APIUtils.baseUrlAccountsAPI = YamlParser.getYmlValue("API.baseUrlAccounts").toString();
    }

    @Override
    public void createDriver() {
        String userName = YamlParser.getConfigValue("Mobile.global.userName").toString();
        String accessKey = YamlParser.getConfigValue("Mobile.global.accessKey").toString();
        String app = YamlParser.getConfigValue("app").toString();
        DesiredCapabilities caps = new DesiredCapabilities();
        final List<String> processArgs = new ArrayList<>(Arrays.asList("-automation-test", "true"));
        JSONObject argsValue = new JSONObject();
        argsValue.put("args", processArgs);

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
