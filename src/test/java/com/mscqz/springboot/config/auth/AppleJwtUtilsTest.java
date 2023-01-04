package com.mscqz.springboot.config.auth;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

@Slf4j
public class AppleJwtUtilsTest {
    @Test
    @DisplayName("exp - iat 값 체크")
    public void check_ExpMinusIatValue() {
        Date exp = new Date();
        exp.setTime(1577943613);
        Date iat = new Date();
        iat.setTime(1577943013);
        log.info("exp-iat 값 : {}", exp.getTime()-iat.getTime()); // 600임
    }
}
