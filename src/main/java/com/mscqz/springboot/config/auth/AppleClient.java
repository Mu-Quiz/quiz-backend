package com.mscqz.springboot.config.auth;

import com.mscqz.springboot.config.auth.dto.ApplePublicKeyResponseDto;
import com.mscqz.springboot.config.auth.dto.AppleToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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

    @PostMapping(value = "/api/allow/apple/token", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    public AppleToken.Response getToken(@RequestBody AppleToken.Request request){
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);  // 읽기시간초과, ms
        factory.setConnectTimeout(3000); // 연결시간초과, ms
        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(100) // connection pool 적용
                .setMaxConnPerRoute(5) // connection pool 적용
                .build();
        factory.setHttpClient(httpClient); // 동기실행에 사용될 HttpClient 세팅
        RestTemplate restTemplate = new RestTemplate(factory);

        ResponseEntity<AppleToken.Response> responseEntity = restTemplate.postForEntity("https://appleid.apple.com/auth/token", request, AppleToken.Response.class);
        log.info("Apple Login Token Response getBody 확인 : {}",responseEntity.getBody());
        return responseEntity.getBody();
    }
}
