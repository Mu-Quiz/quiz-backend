package com.mscqz.springboot.config.auth;

import com.mscqz.springboot.config.auth.dto.ApplePublicKeyResponseDto;
import com.mscqz.springboot.config.auth.dto.AppleToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@RestController
@Slf4j
public class AppleClient {
    @GetMapping("/api/allow/apple/public/key")
    public ApplePublicKeyResponseDto getApplePublicKey(){
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
        log.info("Apple Login Public Key Response getBody 확인 : {}",responseEntity.getBody());
        return responseEntity.getBody();
    }

    // 애플 서버에서 authorization_code 검증
    @PostMapping(value = "/api/allow/apple/token", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    public AppleToken.Response checkAuthorizationCode(@RequestBody AppleToken.Request request) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);  // 읽기시간초과, ms
        factory.setConnectTimeout(3000); // 연결시간초과, ms
        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(100) // connection pool 적용
                .setMaxConnPerRoute(5) // connection pool 적용
                .build();
        factory.setHttpClient(httpClient); // 동기실행에 사용될 HttpClient 세팅

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", request.getGrant_type());
        params.add("client_id", request.getClient_id());
        params.add("client_secret", request.getClient_secret());
        params.add("code", request.getCode());

        RestTemplate restTemplate = new RestTemplate(factory);

        AppleToken.Response responseEntity = restTemplate.postForObject("https://appleid.apple.com/auth/token", params, AppleToken.Response.class);
        log.info("Apple Login Token Response getBody 확인 : {}",responseEntity);
        return responseEntity;
    }

    // 애플 서버에서 refresh_token 검증
    @PostMapping(value = "/api/allow/apple/refresh", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    public AppleToken.Response checkRefreshToken(@RequestBody AppleToken.Request request) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);  // 읽기시간초과, ms
        factory.setConnectTimeout(3000); // 연결시간초과, ms
        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(100) // connection pool 적용
                .setMaxConnPerRoute(5) // connection pool 적용
                .build();
        factory.setHttpClient(httpClient); // 동기실행에 사용될 HttpClient 세팅

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", request.getGrant_type());
        params.add("client_id", request.getClient_id());
        params.add("client_secret", request.getClient_secret());
        params.add("refresh_token", request.getRefresh_token());

        RestTemplate restTemplate = new RestTemplate(factory);

        AppleToken.Response responseEntity = restTemplate.postForObject("https://appleid.apple.com/auth/token", params, AppleToken.Response.class);
        log.info("Apple Login Token Response getBody 확인 : {}",responseEntity);
        return responseEntity;
    }
}
