package project.flows;

import org.openqa.selenium.remote.DesiredCapabilities;
import project.api.APIUtils;
import project.common.YamlParser;

import static project.mobile.DriverInstance.quitDriver;
import static project.plugins.InitializePlugin.*;

public class LocalRunFlow implements RunFlow {

    @Override
    public void getAPIBaseUrl() {
        APIUtils.setBaseUrlAccountsAPI(YamlParser.getYmlValue("API.baseUrlAccounts").toString());
    }

    @Override
    public void createDriver() {
        String userName = YamlParser.getConfigValue("Mobile.global.userName").toString();
        String accessKey = YamlParser.getConfigValue("Mobile.global.accessKey").toString();
        String app = YamlParser.getConfigValue("app").toString();

        DesiredCapabilities caps = CapabilityBuilder.buildCommonCapabilities();
        CapabilityBuilder.addBrowserStackOptions(caps, userName, accessKey);

        platform.createDriverForPlatform(caps, userName, accessKey, app);
    }

    @Override
    public void afterScenario() {
        quitDriver();
    }
}
