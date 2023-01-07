package com.mscqz.springboot.config.auth;

import com.mscqz.springboot.app.dto.LoginRequestDto;
import com.mscqz.springboot.app.dto.LoginResponseDto;
import com.mscqz.springboot.app.exception.CustomException;
import com.mscqz.springboot.config.auth.dto.AppleToken;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.mscqz.springboot.app.exception.ErrorCode.*;

@RequiredArgsConstructor
@RestController
@Slf4j
public class AppleLoginController {
    private final AppleJwtUtils appleJwtUtils;
    private final AppleLoginService appleLoginService;


    // apple login
    @PostMapping("/api/v1/apple/login")
    public ResponseEntity<LoginResponseDto> appleLogin(@RequestBody LoginRequestDto loginRequestDto) throws Exception {
        Claims claims = appleJwtUtils.getClaimsBy(loginRequestDto.getIdentityToken()); // identityToken으로 사용자 정보 받아오기
        if (loginRequestDto.getRefreshToken().length() != 0 || (loginRequestDto.getIdentityToken().length() != 0 && loginRequestDto.getAuthorizationCode().length() != 0)) {
            if(loginRequestDto.getRefreshToken().length() != 0){ // 재로그인
                // DB에서 해당 이메일의 refreshToken 비교해 refreshToken 유효한지 확인
                String email = appleJwtUtils.getEmailByClaims(claims);
                if(appleLoginService.existsUser(email, loginRequestDto) && appleLoginService.checkRefreshTokenByApple(loginRequestDto)){
                    return new ResponseEntity<>(new LoginResponseDto(email, loginRequestDto.getRefreshToken()), HttpStatus.OK); // 200
                }
                else { // refreshToken 이 유효하지 않은 경우
                    throw new CustomException(INVALID_REFRESH_TOKEN); // 401
                }
            }
            else { // 처음 로그인(회원가입)
                // 새로운 사용자 생성
                return new ResponseEntity<>(appleLoginService.signUpWithApple(loginRequestDto, claims), HttpStatus.CREATED); // 201
            }
        }
        else { // 잘못된 로그인 요청(요청 값이 제대로 오지 않은 경우)
            throw new CustomException(MISMATCH_LOGIN_REQUEST); // 400
        }
    }

    // getClaimsBy 메소드 확인용 API (확인 완료)
    @GetMapping("/api/claim")
    public Claims getClaimsBy(){
        String identityToken = "identityToken";
        return appleJwtUtils.getClaimsBy(identityToken);
    }

    // client secret Apple 서버에서 허용해주는지 확인용 API (확인 필요)
    @GetMapping("/api/client/secret")
    public String createClientSecret() throws Exception{
        return appleJwtUtils.createClientSecret();
    }

    // Authorization_code 검증 잘하는지 확인용 API (확인 완료)
    @PostMapping("/api/apple/token/request")
    public AppleToken.Response verifyAuthorizationCode() throws Exception {
        String clientSecret = createClientSecret();
        String code = "authorizationCode";
        return appleJwtUtils.getTokenByCode(clientSecret, code);
    }

    // Refresh_token 유효성 검증 잘하는지 확인용 API (확인 완료)
    @PostMapping("/api/apple/token/refresh")
    public AppleToken.Response verifyRefreshToken() throws Exception {
        String clientSecret = createClientSecret();
        String refreshToken = "refreshToken";
        return appleJwtUtils.getTokenByRefreshToken(clientSecret, refreshToken);
    }
}
