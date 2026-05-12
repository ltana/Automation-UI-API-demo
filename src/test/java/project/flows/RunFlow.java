package project.flows;

import java.net.MalformedURLException;

public interface RunFlow {

    void createDriver() throws MalformedURLException;

    void getAPIBaseUrl();

    void afterScenario();

    default void beforeScenario() {
    }
}
