package project.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;

import static project.common.ConfigReader.setLogConfig;

@CucumberOptions(
        plugin = {"project.plugins.InitializePlugin",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
        features = "feature",
        glue = {"project"},
        monochrome = true
       )
public class APIRegressionRunner extends AbstractTestNGCucumberTests {

    @BeforeTest
    public void setXmlPar(ITestContext testContext) {
        setLogConfig();
    }
}
