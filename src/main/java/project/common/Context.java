package project.common;

import io.restassured.response.Response;
import project.api.JWTPayload;
import project.enums.Environments;

import java.util.Map;

public class Context {

    private static final ThreadLocal<Response> response = new ThreadLocal<>();
    private static final ThreadLocal<String> requestBody = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Object>> yamlData = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Object>> configData = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Object>> translationData = new ThreadLocal<>();
    private static final ThreadLocal<YamlParser> yaml = new ThreadLocal<>();
    private static final ThreadLocal<JWTPayload> jwtPayload = new ThreadLocal<>();
    private static final ThreadLocal<Environments> runEnv = new ThreadLocal<>();
    private static final ThreadLocal<Object> runFlow = new ThreadLocal<>();

    public static Response getResponse() {
        return response.get();
    }

    public static String getRequestBody() {
        return requestBody.get();
    }

    public static Map<String, Object> getYamlData() {
        return yamlData.get();
    }

    public static Map<String, Object> getConfigData() {
        return configData.get();
    }

    public static Map<String, Object> getTranslationData() {
        return translationData.get();
    }

    public static YamlParser getYaml() {
        return yaml.get();
    }

    public static JWTPayload getJwtPayload() {
        return jwtPayload.get();
    }

    public static Environments getRunEnv() {
        return runEnv.get();
    }

    public static Object getRunFlow() {
        return runFlow.get();
    }

    public static void setRunFlow(Object value) {
        runFlow.set(value);
    }

    public static void setRunEnv(Environments value) {
        runEnv.set(value);
    }

    public static void setYaml(YamlParser value) {
        yaml.set(value);
    }

    public static void setResponse(Response value) {
        response.set(value);
    }

    public static void setRequestBody(String value) {
        requestBody.set(value);
    }

    public static void setYamlData(Map<String, Object> value) {
        yamlData.set(value);
    }

    public static void setConfigData(Map<String, Object> value) {
        configData.set(value);
    }

    public static void setTranslationData(Map<String, Object> value) {
        translationData.set(value);
    }

    public static void setJwtPayload(JWTPayload value) {
        jwtPayload.set(value);
    }
}
