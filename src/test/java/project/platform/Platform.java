package project.platform;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;

import static project.mobile.DriverInstance.getDriver;

public abstract class Platform {

    public static String xmlDeviceName;
    public static String xmlDeviceOsVersion;
    public static String xmlDeviceLanguage = "deviceLanguage";
    public static String xmlAppLanguage = "appLanguage";
    public static String xmlDeviceLocale = "deviceLocale";

    public abstract void createDriverForPlatform(DesiredCapabilities caps, String userName, String accessKey, String app)
        throws MalformedURLException;

    public void hideKeyboard() {
        getDriver().hideKeyboard();
    }
}
