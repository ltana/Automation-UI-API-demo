package project.mobile.steps;

import io.cucumber.java.*;
import org.openqa.selenium.OutputType;

import java.net.MalformedURLException;

import static project.api.APIUtils.logger;
import static project.mobile.DriverInstance.getDriver;
import static project.plugins.InitializePlugin.*;

public class TestInitialize {
    static ThreadLocal<String> currentTest = new ThreadLocal<>();

    @BeforeAll
    public static void beforeall() {
        runFlow.getAPIBaseUrl();
    }

    @Before("@Mobile")
    public void beforeScenarioMobile(Scenario scenario) throws MalformedURLException {
        logger.get().info("\n---------------------------------------------------------");
        logger.get().info("Mobile scenario running is: " + scenario.getName() + "\n");
        testName.set(scenario.getName());

        currentTest.set(scenario.getName());
        runFlow.createDriver();
        getDriver();
        runFlow.beforeScenario();
    }

    @Before("@API")
    public void beforeScenarioAPI(Scenario scenario) {
        logger.get().info("\n---------------------------------------------------------");
        logger.get().info("API scenario running is: " + scenario.getName() + "\n");
        testName.set(scenario.getName());
        currentTest.set(scenario.getName());
    }

    @After("@Mobile")
    public void afterScenarioMobile(Scenario scenario) {
        if (scenario.isFailed()) {
            logger.get().info("Mobile scenario:" + scenario.getName() + " --> Failed!");
            takeScreenShot(scenario);
        } else {
            logger.get().info("Mobile scenario:" + scenario.getName() + " --> Passed!");
        }
        logger.get().info("\n---------------------------------------------------------");
        runFlow.afterScenario();
    }

    @After("@API")
    public void afterScenarioAPI(Scenario scenario) {
        if (scenario.isFailed()) {
            logger.get().info("API scenario:" + scenario.getName() + " --> Failed!");
        } else {
            logger.get().info("API scenario:" + scenario.getName() + " --> Passed!");
        }
        logger.get().info("\n---------------------------------------------------------");
    }

    public void takeScreenShot(Scenario scenario) {
        logger.get().info("Take screenshot");
        try {
            final byte[] screenshot = getDriver()
                .getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
