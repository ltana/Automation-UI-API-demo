package project.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static project.common.Context.getJwtPayload;
import static project.common.Context.getRequestBody;

public class JWTUtils {

    private static PrivateKey PRIVATE_KEY;
    public static final String BEARER_PART = "Bearer ";

    public static String deviceProfile = "profile";
    private static ObjectMapper OBJECT_MAPPER;

    public static JWTPayload defaultJWTPayload() {
        long now = new Date().toInstant().getEpochSecond();
        String deviceId = "device111";
        return JWTPayload.builder()
                .iat(now)
                .nbf(now - 1000)
                .exp(now + 10 * 60 * 60)
                .deviceProfile(String.format(
                    deviceProfile, deviceId))
                .build();
    }

    @SneakyThrows
    public static String generateJwt(JWTPayload payload) {
        if (PRIVATE_KEY == null) {
            PRIVATE_KEY = loadPrivateKeyFromFile();
        }
        if (OBJECT_MAPPER == null) {
            OBJECT_MAPPER = new ObjectMapper();
        }

        return BEARER_PART + Jwts.builder()
            .header().add(Map.of("typ", "JWT",
                "kid", "id.key.for.signing.jwt",
                "alg", "RS512"))
            .and()
            .content(OBJECT_MAPPER.writeValueAsString(payload))
            .signWith(PRIVATE_KEY)
            .compact();
    }

    private static PrivateKey loadPrivateKeyFromFile() throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(System.getenv("JWT_PRIVAT_KEY"));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
}
