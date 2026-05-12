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

    @Given("User set request body to {string} from {string} folder")
    public void userSetRequestBodyFromFile(String requestFile, String fileFolder) {
        setRequestBody(getFile("requests", fileFolder, requestFile));
        if (getRunEnv().equals(Environments.UAT)) {

            JsonObject jsonObject = JsonParser.parseString(getRequestBody()).getAsJsonObject();

            JsonObject headerObject = jsonObject.getAsJsonObject("header");

            String userId = headerObject.get("userId").getAsString();

            JWTPayload jwtPayload = defaultJWTPayload();
            jwtPayload.setUserId(userId);
            jwtPayload.setDeviceProfile(String.format(
                deviceProfile));
            setJwtPayload(jwtPayload);
        }
    }

    @Given("User set request body without parameters:$")
    public void userSetRequestBodyWithoutParameter(DataTable testData) {
        String body = getRequestBody();
        List<Map<String, String>> requestParameters = testData.asMaps(String.class, String.class);
        for (Map<String, String> requestParameter : requestParameters) {
            if ((getRunEnv().equals(Environments.UAT)) &&
                (requestParameter.get("requestParameter").equals("header.userId") )){

                body = new JSONHelper()
                    .deleteJsonParameter(body, requestParameter.get("requestParameter"));

                if (requestParameter.get("requestParameter").equals("header.userId")) {
                    JWTPayload jwtPayload = getJwtPayload();

                    JWTPayload updatedPayload = new JWTPayload();

                    Field[] fields = JWTPayload.class.getDeclaredFields();

                    for (Field field : fields) {
                        field.setAccessible(true);
                        if (!field.getName().equals("userId")) {
                            try {
                                field.set(updatedPayload, field.get(jwtPayload));
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    setJwtPayload(updatedPayload);
                } else if (requestParameter.get("requestParameter").equals("header.userId")) {
                    JWTPayload jwtPayload = getJwtPayload();
                    JWTPayload updatedPayload = new JWTPayload();

                    Field[] fields = JWTPayload.class.getDeclaredFields();

                    for (Field field : fields) {
                        field.setAccessible(true);
                        if (!field.getName().equals("userId")) {
                            try {
                                field.set(updatedPayload, field.get(jwtPayload));
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    setJwtPayload(updatedPayload);
                } else if (requestParameter.get("requestParameter").equals("paramenter")) {
                    JWTPayload jwtPayload = getJwtPayload();

                    String removedPart = "\"identifier\":\"%s\",";
                    String modifiedDeviceProfile = deviceProfile.replace(removedPart, "");

                    jwtPayload.setDeviceProfile(String.format(
                        modifiedDeviceProfile, ""));

                    setJwtPayload(jwtPayload);
                }
            } else {
                body = new JSONHelper()
                    .deleteJsonParameter(body, requestParameter.get("requestParameter"));
            }
        }
        setRequestBody(body);
    }

    @And("User sets request with parameters:$")
    public void userSetsRequestWithParameters(DataTable testData) {
        String body = getRequestBody();
        List<Map<String, String>> requestParameters = testData.asMaps(String.class, String.class);
        for (Map<String, String> requestParameter : requestParameters) {
            if ((getRunEnv().equals(Environments.UAT)) &&
                (requestParameter.get("requestParameter").equals("header.userId"))) {
                body = new JSONHelper()
                    .updateJsonValue(body, requestParameter.get("requestParameter"),
                        requestParameter.get("requestValue"));

                if (requestParameter.get("requestParameter").equals("header.userId")) {
                    JWTPayload jwtPayload = getJwtPayload();
                    setJwtPayload(jwtPayload);
                } else if (requestParameter.get("requestParameter").equals("paramenter")) {
                    JWTPayload jwtPayload = getJwtPayload();
                    String modifiedDeviceProfile = deviceProfile;
                    if (requestParameter.get("requestValue") == null) {
                        modifiedDeviceProfile = deviceProfile.replace("\"%s\"", "%s");
                    }
                    jwtPayload.setDeviceProfile(String.format(
                        modifiedDeviceProfile, requestParameter.get("requestValue")));
                    setJwtPayload(jwtPayload);
                }
            } else {
                body = new JSONHelper()
                    .updateJsonValue(body, requestParameter.get("requestParameter"),
                        requestParameter.get("requestValue"));
            }
        }
        setRequestBody(body);
    }

    @When("User makes a POST request to {string} resource from {string} endpoint")
    public void postRequestTo(String pathURL, String endpoint) {
        String body = getRequestBody();
        body = new JSONHelper()
            .deleteJsonParameter(body, "header.userId");
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
    public void responseHasNextReturnedValues(DataTable testData)  {
        var response = getResponse().getBody();

        List<Map<String, String>> responseParameters = testData.asMaps(String.class, String.class);

        for (Map<String, String> responseParameter : responseParameters) {
            var responseValueType = response.jsonPath().get(responseParameter.get("responseParameter")).getClass().getSimpleName();
            switch (responseValueType) {
                case "Float" -> {
                    Float fLoatValue = new JSONHelper().parsStringToFloat(responseParameter.get("responseValue"));
                    assertEquals(response.jsonPath().get(responseParameter.get("responseParameter")),
                        fLoatValue,
                        "Response parameter " + responseParameter.get("responseParameter") + " is not "
                            + responseParameter.get("responseValue"));
                }
                case "Integer" -> {
                    Integer integerValue = new JSONHelper().parsStringToInt(responseParameter.get("responseValue"));
                    assertEquals(response.jsonPath().get(responseParameter.get("responseParameter")),
                        integerValue,
                        "Response parameter " + responseParameter.get("responseParameter") + " is not "
                            + responseParameter.get("responseValue"));
                }
                case "Boolean" -> {
                    Boolean boolValue = new JSONHelper().parseStringToBoolean(responseParameter.get("responseValue"));
                    assertEquals(response.jsonPath().get(responseParameter.get("responseParameter")),
                        boolValue,
                        "Response parameter " + responseParameter.get("responseParameter") + " is not "
                            + responseParameter.get("responseValue"));
                }
                case "Double" -> {
                    Double doubleValue = new JSONHelper().parsStringToDouble(responseParameter.get("responseValue"));
                    assertEquals(response.jsonPath().get(responseParameter.get("responseParameter")),
                        doubleValue,
                        "Response parameter " + responseParameter.get("responseParameter") + " is not "
                            + responseParameter.get("responseValue"));
                }
                case "Long" -> {
                    Long longValue = new JSONHelper().parsStringToLong(responseParameter.get("responseValue"));
                    assertEquals(response.jsonPath().get(responseParameter.get("responseParameter")),
                        longValue,
                        "Response parameter " + responseParameter.get("responseParameter") + " is not "
                            + responseParameter.get("responseValue"));
                }
                default -> {
                    String expectedResponseValue = responseParameter.get("responseValue");
                    String actualResponseValue = response.jsonPath().get(responseParameter.get("responseParameter")).toString().strip();
                    assertEquals(actualResponseValue,
                        expectedResponseValue,
                        "Response parameter [%s] is not [%s]".formatted(actualResponseValue, expectedResponseValue));
                }
            }
        }
    }

    @And("Response json schema equals to {string} from {string} folder")
    public void jsonSchemaEquals(String schemaFile, String fileFolder) {
        Context.getResponse().then().assertThat()
            .body(JsonSchemaValidator
                .matchesJsonSchema(getFile("schemas", fileFolder, schemaFile)));
    }
}
