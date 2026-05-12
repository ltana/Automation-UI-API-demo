package project.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import project.common.Context;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static project.common.Context.getJwtPayload;


public class APIUtils {
    public static ThreadLocal<Logger> logger = ThreadLocal.withInitial(() -> LogManager.getLogger(String.valueOf(Thread.currentThread().getName())));

    public static String baseUrlAccountsAPI;

    public static void postResponse(String body, String pathUrl, String endpoint) {
        setRestAssuredBaseUrl(endpoint);

        String traceId = generateTraceId();
        String[] keyParts = traceId.split("-");
        String key = keyParts[0] + keyParts[keyParts.length - 1];

        // Generate Signature
        String checksum = null;
        try {
            checksum = generateSignature(body, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String jwt = JWTUtils.generateJwt(getJwtPayload());
        Response responsePost = given()
            .contentType(ContentType.JSON)
            .relaxedHTTPSValidation()
            .header("x-project-trace-id", traceId)
            .header("x-project-checksum", checksum)
            .header("Authorization", jwt)
            .when()
            .body(body)
            .post(pathUrl);

        Context.setResponse(responsePost);
    }

    public static synchronized String generateTraceId() {
        UUID uuid = UUID.randomUUID();
        String[] uuidParts = uuid.toString().split("-");
        return String.format("%s-%s-4%s-%s%s-%s", uuidParts[0], uuidParts[1], uuidParts[2].charAt(0), uuidParts[2].charAt(1), uuidParts[3], uuidParts[4]);
    }

    public static synchronized String generateSignature(String payload, String key) throws Exception {
        byte[] payloadBytes = payload.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "HmacSHA512");
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(keySpec);

        byte[] result = mac.doFinal(payloadBytes);

        // Convert the result to Base64
        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        return encoder.encodeToString(result);
    }

    public static void setRestAssuredBaseUrl(String endpoint) {
        if (endpoint.equals("baseUrlAccounts")) {
            RestAssured.baseURI = baseUrlAccountsAPI;
        }
    }

    public static int getStatusCode() {
        Response response = Context.getResponse();
        return response.getStatusCode();
    }
}
