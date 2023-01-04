package com.mscqz.springboot.config.auth;

import com.mscqz.springboot.app.dto.LoginRequestDto;
import com.mscqz.springboot.app.dto.LoginResponseDto;
import com.mscqz.springboot.config.auth.dto.AppleToken;
import com.mscqz.springboot.domain.user.User;
import com.mscqz.springboot.domain.user.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class AppleLoginService {
    private final AppleJwtUtils appleJwtUtils;
    private final UserRepository userRepository;

    // 유효한 identityToken인지 확인
    @Transactional
    public boolean verifyIdentityTokenBeforeSignIn(Claims claims){
        return appleJwtUtils.verifyIdentityToken(claims.getIssuer(), claims.getAudience(), claims.getExpiration(), claims.getIssuedAt());
    }

    // 회원가입(처음 로그인)
    @Transactional
    public LoginResponseDto signUpWithApple(LoginRequestDto loginRequestDto, Claims claims) throws Exception {
        String clientSecret = appleJwtUtils.createClientSecret(); // clientSecret 생성
        AppleToken.Response appleTokenResponse = appleJwtUtils.getTokenByCode(clientSecret, loginRequestDto.getAuthorizationCode());
        String email = appleJwtUtils.getEmailByClaims(claims);
        String refreshToken = appleTokenResponse.getRefreshToken();
        saveNewUserByAppleLogin(claims, refreshToken, loginRequestDto.getName());
        return new LoginResponseDto(email, refreshToken);
    }

    // DB에 Apple login 신규 사용자 정보 저장
    @Transactional
    private void saveNewUserByAppleLogin(Claims claims, String refreshToken, String name) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String email = appleJwtUtils.getEmailByClaims(claims);
        String sub = appleJwtUtils.getSubByClaims(claims);
        User newUser = User.builder()
                .name(name)
                .email(email)
                .userIdentifier(sub)
                .refreshToken(refreshToken)
                .build();

        log.info("DB에 Apple login 신규 사용자 정보 저장");
        userRepository.save(newUser);
    }

    // 유효한 refreshToken인지 APPLE 서버에서 확인
    @Transactional
    public boolean checkRefreshTokenByApple(LoginRequestDto loginRequestDto) throws Exception {
        String clientSecret = appleJwtUtils.createClientSecret(); // clientSecret 생성
        AppleToken.Response appleTokenResponse = appleJwtUtils.getTokenByRefreshToken(clientSecret, loginRequestDto.getRefreshToken());
        if (appleTokenResponse.getAccessToken().length()!=0){
            return true;
        }
        return false;
    }

    // 존재하는 사용자이며, 해당 사용자의 refreshToken이 요청된 refreshToken과 일치하는지 확인
    @Transactional
    public boolean existsUser(String email, LoginRequestDto loginRequestDto) {
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 사용자가 없습니다. email="+ email));
        if(findUser.getEmail().length() != 0 && findUser.getRefreshToken().equals(loginRequestDto.getRefreshToken())){
            return true;
        }
        return false;
    }
}
