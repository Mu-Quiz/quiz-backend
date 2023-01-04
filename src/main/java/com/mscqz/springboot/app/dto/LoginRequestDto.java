package com.mscqz.springboot.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequestDto {
    private String identityToken;
    private String authorizationCode;
    private String refreshToken;
    private String name;

    public LoginRequestDto(String identityToken, String authorizationCode, String refreshToken, String name) {
        this.identityToken = identityToken;
        this.authorizationCode = authorizationCode;
        this.refreshToken = refreshToken;
        this.name = name;
    }
}
