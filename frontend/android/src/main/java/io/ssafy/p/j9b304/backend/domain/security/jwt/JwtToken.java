package io.ssafy.p.j9b304.backend.domain.security.jwt;


import lombok.Builder;
import lombok.Data;

@Data
public class JwtToken {

    private String grantType;
    private String accessToken;
    private String refreshToken;

    @Builder
    public JwtToken(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}