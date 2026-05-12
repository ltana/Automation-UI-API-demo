package project.platform;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

import static project.mobile.DriverInstance.setDriver;

public class Android extends Platform {

    static {
        xmlDeviceName = "deviceNameAndroid";
        xmlDeviceOsVersion = "osVersionAndroid";
    }

    @Override
    public void createDriverForPlatform(DesiredCapabilities caps, String userName, String accessKey, String app)
        throws MalformedURLException {
        caps.setCapability("appium:app", app);
        caps.setCapability("platformName", org.openqa.selenium.Platform.ANDROID.toString());

        setDriver(new AndroidDriver(
            new URL("https://" + userName + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub"), caps));
    }
}
