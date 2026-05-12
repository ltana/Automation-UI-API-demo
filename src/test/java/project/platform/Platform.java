package project.platform;

import org.openqa.selenium.remote.DesiredCapabilities;
import project.mobile.base.BasePage;

public class Platform extends BasePage {
    public static String xmlDeviceName;
    public static String xmlDeviceOsVersion;
    public static String xmlDeviceLanguage = "deviceLanguage";
    public static String xmlAppLanguage = "appLanguage";
    public static String xmlDeviceLocale = "deviceLocale";

    public void createDriverForPlatform(DesiredCapabilities caps, String userName, String accessKey, String app) {
    }
}
