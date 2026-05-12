package project.platform;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

import static project.mobile.DriverInstance.setDriver;

public class IOS extends Platform {

    static {
        xmlDeviceName = "deviceNameiOS";
        xmlDeviceOsVersion = "osVersioniOS";
    }

    @Override
    public void createDriverForPlatform(DesiredCapabilities caps, String userName, String accessKey, String app)
        throws MalformedURLException {
        caps.setCapability("appium:app", app);
        caps.setCapability("platformName", org.openqa.selenium.Platform.IOS.toString());

        setDriver(new IOSDriver(
            new URL("https://" + userName + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub"), caps));
    }
}
