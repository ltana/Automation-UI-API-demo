package project.common;


import org.yaml.snakeyaml.Yaml;
import project.enums.Environments;

import java.io.InputStream;
import java.util.Map;

/**
 * @author shashitiwari
 */
public class YamlParser {

    Yaml yaml;
    InputStream inputStream;
    Map<String, Object> dataMap;

    /**
     * @param env
     * @return
     */
    public void parseYaml(Environments env) {

        yaml = new Yaml();

        switch (env) {
            case TEST:
                inputStream = this.getClass().getClassLoader().getResourceAsStream("testdata_test.yml");
                break;

            case UAT:
                inputStream = this.getClass().getClassLoader().getResourceAsStream("testdata_uat.yml");
                break;

            case PREPROD:
                inputStream = this.getClass().getClassLoader().getResourceAsStream("testdata_preprod.yml");
                break;

            case PROD:
                inputStream = this.getClass().getClassLoader().getResourceAsStream("testdata_prod.yml");
                break;

            default:
                throw new IllegalStateException("invalid environment provided" + env);
        }
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

    /**
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Object getYmlValue(String key) {

        try {
            final String[] tmpKeys = key.split("\\.");

            Map<String, Object> currentMap = Context.getYamlData();

            for (int i = 0; i < tmpKeys.length - 1; i++) {
                currentMap = (Map<String, Object>) currentMap.get(tmpKeys[i]);
            }
            return currentMap.get(tmpKeys[tmpKeys.length - 1]);
        } catch (Exception exception) {
            return ' ';
        }
    }

    /**
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Object getConfigValue(String key) {
        try {
            final String[] tmpKeys = key.split("\\.");
            Map<String, Object> currentMap = Context.getConfigData();
            for (int i = 0; i < tmpKeys.length - 1; i++) {
                currentMap = (Map<String, Object>) currentMap.get(tmpKeys[i]);
            }
            return currentMap.get(tmpKeys[tmpKeys.length - 1]);
        } catch (Exception exception) {
            return ' ';
        }
    }

    public static Object getTranslationValue(String key) {
        try {
            final String[] tmpKeys = key.split("\\.");
            Map<String, Object> currentMap = Context.getTranslationData();
            for (int i = 0; i < tmpKeys.length - 1; i++) {
                currentMap = (Map<String, Object>) currentMap.get(tmpKeys[i]);
            }
            return currentMap.get(tmpKeys[tmpKeys.length - 1]);
        } catch (Exception exception) {
            return ' ';
        }
    }
}
