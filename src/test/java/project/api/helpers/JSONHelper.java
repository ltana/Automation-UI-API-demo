package project.api.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import project.api.APIUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.jsonpath.JsonPath.read;
import static com.jayway.jsonpath.JsonPath.using;
import static project.plugins.InitializePlugin.logger;

public class JSONHelper {

    private static List<String> savedResponseValues;

    private static final Map<String, Object> savedRequestValue = new HashMap<>();

    public static String getFile(String category, String folder, String fileName) {
        var file = APIUtils.class
            .getClassLoader()
            .getResourceAsStream(category + File.separator + folder.replace("/", File.separator) + File.separator + fileName);
        DocumentContext context = null;
        try {
            assert file != null;
            context = getJsonDocumentContext(new String(file.readAllBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return context.jsonString();
    }

    private static DocumentContext getJsonDocumentContext(String json) {
        var configuration = Configuration.builder()
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .mappingProvider(new JacksonMappingProvider())
            .build();
        return using(configuration).parse(json);
    }

    public String updateJsonValue(String json, String jsonKey, Object newValue) {
        DocumentContext context = getJsonDocumentContext(json);
        try {
            var o = read(json, jsonKey);
            var valueType = o.getClass().getSimpleName();

            switch (valueType) {
                case "Integer" -> {
                    Integer integerValue = parsStringToInt(String.valueOf(newValue));
                    if (integerValue != null)
                        context.set(jsonKey, integerValue);
                }
                case "Boolean" -> {
                    Boolean boolValue = parseStringToBoolean(String.valueOf(newValue));
                    if (boolValue != null)
                        context.set(jsonKey, boolValue);
                }
                case "Float" -> {
                    Float floatValue = parsStringToFloat(String.valueOf(newValue));
                    if (floatValue != null)
                        context.set(json, floatValue);
                }
                case "Double" -> {
                    Double doubleValue = parsStringToDouble(String.valueOf(newValue));
                    if (doubleValue != null)
                        context.set(jsonKey, doubleValue);
                }
                case "Long" -> {
                    Long longValue = parsStringToLong(String.valueOf(newValue));
                    if (longValue != null)
                        context.set(jsonKey, longValue);
                }
                case "JSONArray" -> {
                    // Handle array values
                    List<?> listValue = parseStringToList(String.valueOf(newValue));
                    if (listValue != null) {
                        context.set(jsonKey, listValue);
                    } else {
                        context.set(jsonKey, newValue);
                    }
                }
                default -> context.set(jsonKey, newValue);
            }
            return context.jsonString();
        } catch (PathNotFoundException je) {
            logger.get().warn("{} couldn't be found, in JSON \n{}", jsonKey, json);
            return json;
        }
    }

    private List<?> parseStringToList(String stringValue) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(stringValue.split(","), List.class);

    }

    public String deleteJsonParameter(String json, String jsonKey) {
        DocumentContext context = getJsonDocumentContext(json);
        try {
            context.delete(jsonKey);
            return context.jsonString();
        } catch (PathNotFoundException je) {
            logger.get().warn("{} couldn't be found, in JSON \n{}", jsonKey, json);
            return json;
        }
    }

    public Integer parsStringToInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.get().warn("Found json value is integer, but the {} isn't parsable to integer, " +
                "please set a new integer value for update.", value);
        }
        return null;
    }

    public Boolean parseStringToBoolean(String value) {
        if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("true")) {
            return Boolean.parseBoolean(value);
        } else {
            logger.get().warn("Found json value is boolean, but the {}, isn't boolean string, please set " +
                "a new ture or false string for update", value);
        }
        return false;
    }

    public Float parsStringToFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            logger.get().warn("Found json value is float, but the {} isn't parsable to float, " +
                "please set a new float value for update.", value);
        }
        return null;
    }

    public Double parsStringToDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            logger.get().warn("Found json value is double, but the {} isn't parsable to double, " +
                "please set a new double value for update.", value);
        }
        return null;
    }

    public Long parsStringToLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            logger.get().warn("Found json value is long, but the {} isn't parsable to long, " +
                "please set a new long value for update.", value);
        }
        return null;
    }
}
