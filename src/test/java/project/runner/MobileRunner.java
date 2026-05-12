package project.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;

import static project.common.ConfigReader.*;
import static project.common.ConfigReader.setThreadName;

@CucumberOptions(
        plugin = {"project.plugins.InitializePlugin",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
        features = "feature",
        glue = {"project"},
        monochrome = true,
        tags = "@Mobile and not (@KnownIssue or @Ignore)")
public class MobileRunner extends AbstractTestNGCucumberTests {

    @BeforeTest
    public void setXmlPar(ITestContext testContext) throws NoSuchMethodException {
        setLogConfig();
        setPlatform(testContext);
        setAppiumCap(testContext);
        setThreadName();
    }
}
