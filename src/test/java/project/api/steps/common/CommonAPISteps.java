package project.api.steps.common;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import project.api.JWTPayload;
import project.api.helpers.JSONHelper;
import project.common.Context;
import project.enums.Environments;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static project.api.APIUtils.getStatusCode;
import static project.api.APIUtils.postResponse;
import static project.api.JWTUtils.defaultJWTPayload;
import static project.api.JWTUtils.deviceProfile;
import static project.api.helpers.JSONHelper.getFile;
import static project.common.Context.*;

public class CommonAPISteps {

    private final JSONHelper jsonHelper = new JSONHelper();

    @Given("User set request body to {string} from {string} folder")
    public void userSetRequestBodyFromFile(String requestFile, String fileFolder) {
        setRequestBody(getFile("requests", fileFolder, requestFile));
        if (isUatEnvironment()) {
            JsonObject jsonObject = JsonParser.parseString(getRequestBody()).getAsJsonObject();
            JsonObject headerObject = jsonObject.getAsJsonObject("header");
            String userId = headerObject.get("userId").getAsString();

            JWTPayload jwtPayload = defaultJWTPayload();
            jwtPayload.setUserId(userId);
            jwtPayload.setDeviceProfile(String.format(deviceProfile));
            setJwtPayload(jwtPayload);
        }
    }

    @Given("User set request body without parameters:$")
    public void userSetRequestBodyWithoutParameter(DataTable testData) {
        String body = getRequestBody();
        List<Map<String, String>> requestParameters = testData.asMaps(String.class, String.class);

        for (Map<String, String> requestParameter : requestParameters) {
            String param = requestParameter.get("requestParameter");
            body = jsonHelper.deleteJsonParameter(body, param);

            if (isUatEnvironment()) {
                adjustJwtAfterRemoval(param);
            }
        }
        setRequestBody(body);
    }

    @And("User sets request with parameters:$")
    public void userSetsRequestWithParameters(DataTable testData) {
        String body = getRequestBody();
        List<Map<String, String>> requestParameters = testData.asMaps(String.class, String.class);

        for (Map<String, String> requestParameter : requestParameters) {
            String param = requestParameter.get("requestParameter");
            String value = requestParameter.get("requestValue");
            body = jsonHelper.updateJsonValue(body, param, value);

            if (isUatEnvironment()) {
                adjustJwtAfterUpdate(param, value);
            }
        }
        setRequestBody(body);
    }

    @When("User makes a POST request to {string} resource from {string} endpoint")
    public void postRequestTo(String pathURL, String endpoint) {
        String body = getRequestBody();
        body = jsonHelper.deleteJsonParameter(body, "header.userId");
        setRequestBody(body);
        postResponse(getRequestBody(), pathURL, endpoint);
    }

    @Then("Status code equals {int}")
    public void statusCodeIs(int statusCode) {
        assertEquals(getStatusCode(), statusCode,
            "Status code is not " + statusCode
                + ", response is " + getResponse().asPrettyString());
    }

    @And("Response has next returned values:$")
    public void responseHasNextReturnedValues(DataTable testData) {
        var response = getResponse().getBody();
        List<Map<String, String>> responseParameters = testData.asMaps(String.class, String.class);

        for (Map<String, String> responseParameter : responseParameters) {
            String paramPath = responseParameter.get("responseParameter");
            String expectedStr = responseParameter.get("responseValue");

            Object actualValue = response.jsonPath().get(paramPath);
            String valueType = actualValue.getClass().getSimpleName();

            Object expectedValue = jsonHelper.parseValueToType(expectedStr, valueType);
            if ("String".equals(valueType)) {
                actualValue = actualValue.toString().strip();
            }

            assertEquals(actualValue, expectedValue,
                "Response parameter [%s] is not [%s]".formatted(paramPath, expectedStr));
        }
    }

    @And("Response json schema equals to {string} from {string} folder")
    public void jsonSchemaEquals(String schemaFile, String fileFolder) {
        Context.getResponse().then().assertThat()
            .body(JsonSchemaValidator
                .matchesJsonSchema(getFile("schemas", fileFolder, schemaFile)));
    }

    private boolean isUatEnvironment() {
        return Environments.UAT.equals(getRunEnv());
    }

    private void adjustJwtAfterRemoval(String param) {
        if ("header.userId".equals(param)) {
            setJwtPayload(copyJwtPayloadWithoutField(getJwtPayload(), "userId"));
        } else if ("paramenter".equals(param)) {
            JWTPayload jwtPayload = getJwtPayload();
            String removedPart = "\"identifier\":\"%s\",";
            String modifiedDeviceProfile = deviceProfile.replace(removedPart, "");
            jwtPayload.setDeviceProfile(String.format(modifiedDeviceProfile, ""));
            setJwtPayload(jwtPayload);
        }
    }

    private void adjustJwtAfterUpdate(String param, String value) {
        if ("header.userId".equals(param)) {
            setJwtPayload(getJwtPayload());
        } else if ("paramenter".equals(param)) {
            JWTPayload jwtPayload = getJwtPayload();
            String modifiedDeviceProfile = deviceProfile;
            if (value == null) {
                modifiedDeviceProfile = deviceProfile.replace("\"%s\"", "%s");
            }
            jwtPayload.setDeviceProfile(String.format(modifiedDeviceProfile, value));
            setJwtPayload(jwtPayload);
        }
    }

    private JWTPayload copyJwtPayloadWithoutField(JWTPayload source, String excludeField) {
        JWTPayload copy = new JWTPayload();
        for (Field field : JWTPayload.class.getDeclaredFields()) {
            field.setAccessible(true);
            if (!field.getName().equals(excludeField)) {
                try {
                    field.set(copy, field.get(source));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return copy;
    }
}
