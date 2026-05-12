package project.common;

import org.apache.logging.log4j.core.config.Configurator;
import org.testng.ITestContext;

import java.net.URI;

import project.platform.Platform;
import static project.plugins.InitializePlugin.*;


public class ConfigReader {

    public static synchronized void setAppiumCap(ITestContext testContext) {
        try {
            setStringPropertyValueFromXml(deviceName, Platform.xmlDeviceName, testContext);
        } catch (Exception e) {
            logger.get().info(e.getMessage());
        }
        try {
            setStringPropertyValueFromXml(osVersion, Platform.xmlDeviceOsVersion, testContext);
        } catch (Exception e) {
            logger.get().info(e.getMessage());
        }
        try {
            setStringPropertyValueFromXml(deviceLanguage, Platform.xmlDeviceLanguage, testContext);
        } catch (Exception e) {
            logger.get().info(e.getMessage());
        }
        try {
            setStringPropertyValueFromXml(appLanguage, Platform.xmlAppLanguage, testContext);
        } catch (Exception e) {
            logger.get().info(e.getMessage());
        }
        try {
            setStringPropertyValueFromXml(deviceLocale, Platform.xmlDeviceLocale, testContext);
        } catch (Exception e) {
            logger.get().info(e.getMessage());
        }
    }

    public static synchronized void setStringPropertyValueFromXml(ThreadLocal<String> localListProperty, String xmlParameterName,
                                                                  ITestContext testContext) {
        if (!testContext.getCurrentXmlTest().getParameter(xmlParameterName).isEmpty()) {
            localListProperty.set(testContext
                    .getCurrentXmlTest()
                    .getParameter(xmlParameterName));
        } else {
            throw new AssertionError("parameter is xml is empty - " + xmlParameterName);
        }
    }

    public static synchronized void setThreadName() {
        Thread.currentThread().setName(deviceName.get());
    }

    public static synchronized void setLogConfig() {
        Configurator.reconfigure(URI.create("classpath:log4j2.properties"));
    }

    public static void setPlatform(ITestContext testContext) {
        try {
            setStringPropertyValueFromXml(osType, "osType", testContext);
        } catch (Exception e) {
            logger.get().info(e.getMessage());
        }
        platform = applicationContext.getBean(osType.get(), Platform.class);
    }
}
