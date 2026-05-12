package project.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import project.platform.Android;
import project.platform.IOS;
import project.platform.Platform;

@Configuration
@ComponentScan(value = {"project"})
public class AppConfig {

    @Bean("android")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Platform platformAndroid() {
        return new Android();
    }

    @Bean("ios")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Platform platformIOS() {return new IOS();}
}
