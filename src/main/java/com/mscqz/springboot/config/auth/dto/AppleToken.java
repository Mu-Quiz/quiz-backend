package com.mscqz.springboot.config.auth.dto;

import lombok.Getter;
import lombok.Setter;

public class AppleToken {
    @Setter
    @Getter
    public static class Request {
        private String code;
        private String client_id; // bundle 적어주면 됨
        private String client_secret;
        private String grant_type;
        private String refresh_token;

        public static Request ofCode(String clientId, String clientSecret, String code, String grantType){
            Request request = new Request();
            request.client_id = clientId;
            request.client_secret = clientSecret;
            request.code = code;
            request.grant_type = grantType;
            return request;
        }

        public static Request ofRefreshToken(String clientId, String clientSecret, String grantType, String refreshToken){
            Request request = new Request();
            request.client_id = clientId;
            request.client_secret = clientSecret;
            request.grant_type = grantType;
            request.refresh_token = refreshToken;
            return request;
        }
    }

    @Setter
    public static class Response {
        private String access_token;
        private String expires_in;
        private String id_token;
        private String refresh_token;
        private String token_type;
        private String error;

        public String getAccessToken(){
            return access_token;
        }

        public String getExpiresIn() {
            return expires_in;
        }

        public String getIdToken() {
            return id_token;
        }

        public String getRefreshToken() {
            return refresh_token;
        }

        public String getTokenType(){
            return token_type;
        }

        @Override
        public String toString() {
            return "AppleIdTokenResponse{" +
                    "access_token='" + access_token + '\'' +
                    ", token_type='" + token_type + '\'' +
                    ", expires_in='" + expires_in + '\'' +
                    ", refresh_token='" + refresh_token + '\'' +
                    ", id_token='" + id_token + '\'' +
                    '}';
        }
    }
}
