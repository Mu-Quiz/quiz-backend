package com.mscqz.springboot.app.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String identityToken;
    private String authorizationCode;
}
