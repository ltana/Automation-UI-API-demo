package project.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JWTPayload {

    private String deviceProfile;
    private String sub;
    private long nbf;
    private String iss;
    private long exp;
    private long iat;
    private String userId;
    private String username;
}
