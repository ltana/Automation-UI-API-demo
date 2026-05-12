package project.common;

import io.restassured.response.Response;
import lombok.Getter;
import project.api.JWTPayload;
import project.enums.Environments;

import java.util.Map;
import java.util.Properties;

public class Context {

    @Getter
    private static Response response;
    @Getter
    private static String requestBody;
    @Getter
    private static Map<String, Object> yamlData;
    @Getter
    private static Map<String, Object> configData;
    @Getter
    private static Map<String, Object> translationData;
    @Getter
    private static YamlParser yaml;

    @Getter
    private static JWTPayload jwtPayload;

    @Getter
    public static Environments runEnv;

    public static Object runFlow;

    public static void setRunFlow(Object runFlow) {
        Context.runFlow=runFlow;}

    public static void setRunEnv(Environments runEnv) {
        Context.runEnv=runEnv;
    }

    public static void setYaml(YamlParser yaml) {
        Context.yaml = yaml;
    }

    public static void setResponse(Response response) {
        Context.response = response;
    }

    public static void setRequestBody(String requestBody) {
        Context.requestBody = requestBody;
    }

    public static void setYamlData(Map<String, Object> yamlData) {
        Context.yamlData = yamlData;
    }

    public static void setConfigData(Map<String, Object> configData) {
        Context.configData = configData;
    }

    public static void setTranslationData(Map<String, Object> translationData) {
        Context.translationData = translationData;
    }

    public static void setJwtPayload(JWTPayload jwtPayload) {
        Context.jwtPayload = jwtPayload;
    }
}
