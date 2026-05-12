package project.common;

import org.yaml.snakeyaml.Yaml;
import project.enums.Environments;

import java.io.InputStream;
import java.util.Map;

public class YamlParser {

    Yaml yaml;
    InputStream inputStream;
    Map<String, Object> dataMap;

    public void parseYaml(Environments env) {
        yaml = new Yaml();

        String fileName = switch (env) {
            case TEST -> "testdata_test.yml";
            case UAT -> "testdata_uat.yml";
            case PREPROD -> "testdata_preprod.yml";
            case PROD -> "testdata_prod.yml";
        };

        inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        dataMap = yaml.load(inputStream);
        Context.setYamlData(dataMap);
    }

    public void setConfig() {
        yaml = new Yaml();
        inputStream = this.getClass().getClassLoader().getResourceAsStream("config.yml");
        Context.setConfigData(yaml.load(inputStream));
    }

    public void setTranslations() {
        yaml = new Yaml();
        inputStream = this.getClass().getClassLoader().getResourceAsStream("translations.yml");
        Context.setTranslationData(yaml.load(inputStream));
    }

    @SuppressWarnings("unchecked")
    private static Object getNestedValue(Map<String, Object> dataMap, String key) {
        if (dataMap == null) {
            throw new IllegalStateException("Data map is not initialized for key: " + key);
        }
        final String[] tmpKeys = key.split("\\.");
        Map<String, Object> currentMap = dataMap;
        for (int i = 0; i < tmpKeys.length - 1; i++) {
            Object nested = currentMap.get(tmpKeys[i]);
            if (nested == null) {
                throw new IllegalArgumentException("Key segment '%s' not found in path: %s".formatted(tmpKeys[i], key));
            }
            currentMap = (Map<String, Object>) nested;
        }
        return currentMap.get(tmpKeys[tmpKeys.length - 1]);
    }

    public static Object getYmlValue(String key) {
        return getNestedValue(Context.getYamlData(), key);
    }

    public static Object getConfigValue(String key) {
        return getNestedValue(Context.getConfigData(), key);
    }

    public static Object getTranslationValue(String key) {
        return getNestedValue(Context.getTranslationData(), key);
    }
}
