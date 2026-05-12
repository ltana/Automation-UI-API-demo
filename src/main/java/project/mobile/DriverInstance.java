package project.mobile;

import io.appium.java_client.AppiumDriver;

import java.time.Duration;

public class DriverInstance {

    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    public static final int DEFAULT_IMPLICITLY_WAIT = 10;

    public static void setDriver(AppiumDriver currentDriver) {
            Duration duration = Duration.ofSeconds(DEFAULT_IMPLICITLY_WAIT);
            driver.set((AppiumDriver) currentDriver.setSetting("snapshotMaxDepth", 62));
            driver.get().manage().timeouts().implicitlyWait(duration);
    }

    public static AppiumDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        getDriver().quit();
    }
}
