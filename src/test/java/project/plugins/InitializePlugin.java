package project.plugins;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.Plugin;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestRunStarted;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import project.common.Context;
import project.common.YamlParser;
import project.config.AppConfig;
import project.enums.Environments;
import project.enums.RunContexts;
import project.flows.CIRunFlow;
import project.flows.LocalRunFlow;
import project.flows.RunFlow;
import project.platform.Platform;

public class InitializePlugin implements Plugin, ConcurrentEventListener {

    public static RunContexts runContext;
    public static Environments env;
    public static ThreadLocal<Logger> logger = ThreadLocal.withInitial(() -> LogManager.getLogger(String.valueOf(Thread.currentThread().getName())));

    public static RunFlow runFlow;
    public static Platform platform;

    public static ApplicationContext applicationContext =
            new AnnotationConfigApplicationContext(AppConfig.class);

    public static ThreadLocal<String> osType = new ThreadLocal<>();
    public static ThreadLocal<String> deviceName = new ThreadLocal<>();
    public static ThreadLocal<String> osVersion = new ThreadLocal<>();
    public static ThreadLocal<String> deviceLanguage = new ThreadLocal<>();
    public static ThreadLocal<String> appLanguage = new ThreadLocal<>();
    public static ThreadLocal<String> deviceLocale = new ThreadLocal<>();
    public static ThreadLocal<String> testName = new ThreadLocal<>();

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, setup);
    }

    private final EventHandler<TestRunStarted> setup = event -> {
        try {
            beforeAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    private void beforeAll()  {
        YamlParser yaml = new YamlParser();
        yaml.setTranslations();
        if (System.getenv("RUN_CONTEXT") == null) {
            runContext = RunContexts.LOCAL;
            yaml.setConfig();
            env = Environments.valueOf(YamlParser.getConfigValue("General.env").toString().toUpperCase());
            runFlow = new LocalRunFlow();
        } else {
            runContext = RunContexts.valueOf(System.getenv("RUN_CONTEXT"));
            env = Environments.valueOf(System.getenv("ENV"));
            runFlow = new CIRunFlow();
        }
        yaml.parseYaml(env);

        Context.setYaml(yaml);
        Context.setRunEnv(env);
        Context.setRunFlow(runFlow.getClass().getSimpleName());
    }
}
