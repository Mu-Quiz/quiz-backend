package com.mscqz.springboot.config.auth.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

public class ApplePublicKeyResponseDto {
    @Getter
    @Setter
    private List<Key> keys;

    @Getter
    @Setter
    public static class Key {
        private String kty;
        private String kid;
        private String use;
        private String alg;
        private String n;
        private String e;

        @Override
        public String toString() {
            return "ApplePublicKey{" +
                    "kty='" + kty + '\'' +
                    ", kid='" + kid + '\'' +
                    ", use='" + use + '\'' +
                    ", alg='" + alg + '\'' +
                    ", n='" + n + '\'' +
                    ", e='" + e + '\'' +
                    '}';
        }
    }

    public Optional<Key> getMatchedKeyBy(String kid, String alg){
        return this.keys.stream()
                .filter(key -> key.getKid().equals(kid) && key.getAlg().equals(alg))
                .findFirst();
    }
}
