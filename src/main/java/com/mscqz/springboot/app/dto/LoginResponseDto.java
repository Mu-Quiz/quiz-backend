package com.mscqz.springboot.app.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String email;
    private String refresh_token;

    public LoginResponseDto(String email, String refreshToken) {
        this.email = email;
        this.refresh_token = refreshToken;
    }
}
