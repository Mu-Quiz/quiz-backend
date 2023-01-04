package com.mscqz.springboot.service.music;

import com.mscqz.springboot.config.auth.dto.ApplePublicKeyResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class AppleLoginServiceTest {

    @Test
    @DisplayName("Apple에서 Public Key 받아오기")
    public void test_getForEntity() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);  // 읽기시간초과, ms
        factory.setConnectTimeout(3000); // 연결시간초과, ms
        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(100) // connection pool 적용
                .setMaxConnPerRoute(5) // connection pool 적용
                .build();
        factory.setHttpClient(httpClient); // 동기실행에 사용될 HttpClient 세팅
        RestTemplate restTemplate = new RestTemplate(factory);

        ResponseEntity<ApplePublicKeyResponseDto> responseEntity = restTemplate.getForEntity("https://appleid.apple.com/auth/keys", ApplePublicKeyResponseDto.class);
        List<ApplePublicKeyResponseDto> list = Arrays.asList(responseEntity.getBody());
        System.out.println(list);
        System.out.println("시작");
        System.out.println(responseEntity.getBody());
        System.out.println("끝");
    }
}
