package com.mscqz.springboot.config.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mscqz.springboot.config.auth.dto.ApplePublicKeyResponseDto;
import com.mscqz.springboot.config.auth.dto.AppleToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppleJwtUtils {
    private final AppleClient appleClient;

    @Value("${APPLE.KEY.ID}")
    private String KEY_ID;

    @Value("${APPLE.TEAM.ID}")
    private String TEAM_ID;

    @Value("${APPLE.KEY.PATH}")
    private String KEY_PATH;

    @Value("${APPLE.APP.BUNDLE}")
    private String APP_BUNDLE;

    @Value("${APPLE.ISS}")
    private String APPLE_ISS;

    // identity_token 검증
    public Claims getClaimsBy(String identityToken){
        ApplePublicKeyResponseDto responseDto = appleClient.getApplePublicKey();

        try{
            String headerOfIdentityToken = identityToken.substring(0, identityToken.indexOf("."));
            Map<String, String> header = null;
            try{
                header = new ObjectMapper().readValue(new String(Base64.getDecoder().decode(headerOfIdentityToken), "UTF-8"), Map.class);
            } catch(JsonProcessingException | UnsupportedEncodingException e){
                e.printStackTrace();
            }
            ApplePublicKeyResponseDto.Key key = responseDto.getMatchedKeyBy(header.get("kid"), header.get("alg"))
                    .orElseThrow(()-> new NullPointerException("Failed get public key from apple's id server."));
            byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

            BigInteger n = new BigInteger(1, nBytes);
            BigInteger e = new BigInteger(1, eBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(identityToken).getBody();

        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeySpecException e) {
        } catch (SignatureException | MalformedJwtException e){
            //토큰 서명 검증 or 구조 문제 (Invalid token)
        } catch(ExpiredJwtException e){
            //토큰이 만료됐기 때문에 클라이언트는 토큰을 refresh 해야함.
        } catch(Exception e){
        }
        return null;
    }

    // 이런식으로 claims 사용하면 될 듯
    // Claims에서 email 받아오기 - 저장해야 함
    public String getEmailByClaims(Claims claims) throws NoSuchAlgorithmException, InvalidKeySpecException{
        try{
            return claims.get("email", String.class);
        }
        catch (Exception e){
            System.out.println("Trying for another key");
        }
        return "Claim email 받아오기에서 Error Occured...";
    }

    // Claims에서 sub(유저의 고유한 식별자) 받아오기 - 저장해야 함
    public String getSubByClaims(Claims claims) throws NoSuchAlgorithmException, InvalidKeySpecException{
        try{
            return claims.get("sub", String.class);
        }
        catch (Exception e){
            System.out.println("Trying for another key");
        }
        return "Claim sub 받아오기에서 Error Occured...";
    }

    // client_secret 생성
    public String createClientSecret() throws Exception {
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        log.info("client_secret 생성");
        return Jwts.builder()
                .setHeaderParam("kid", KEY_ID)
                .setHeaderParam("alg", "ES256")
                .setIssuer(TEAM_ID)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .setAudience("https://appleid.apple.com")
                .setSubject(APP_BUNDLE)
                .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                .compact();
    }

    // p8 파일에 있는 private key 가져오기
    private PrivateKey getPrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource(KEY_PATH);
        String privateKey = new String(Files.readAllBytes(Paths.get(resource.getURI())));
        Reader pemReader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        log.info("p8 파일에 있는 private key 가져오기 완료");
        return converter.getPrivateKey(object);
    }

    // identity_token 서명 검증 (데이터 신뢰 검증 메소드)
    public boolean verifyIdentityToken(String iss, String aud, Date exp, Date iat){
        Date now = new Date(System.currentTimeMillis());
        boolean result = now.before(exp);

        if(iss.equals(APPLE_ISS) && aud.equals(APP_BUNDLE) && exp.getTime() - iat.getTime() == 600 && result){
            log.info("identity_token 서명 검증 (데이터 신뢰 검증 메소드) 완료");
            return true;
        }
        return false;
    }

    // Authorization_code 검증
    public AppleToken.Response getTokenByCode(String clientSecret, String code) throws IOException{
        AppleToken.Request request = AppleToken.Request.of(code, APP_BUNDLE, clientSecret, "authorization_code", null);
        return appleClient.getToken(request);
    }

    // Refresh_token 검증
    public AppleToken.Response getTokenByRefreshToken(String clientSecret, String refreshToken) throws IOException{
        AppleToken.Request request = AppleToken.Request.of(null, APP_BUNDLE, clientSecret, "refresh_token", refreshToken);
        return appleClient.getToken(request);
    }

}
