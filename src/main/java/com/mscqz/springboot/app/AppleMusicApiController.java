package com.mscqz.springboot.app;

import com.mscqz.springboot.app.exception.CustomException;
import com.mscqz.springboot.app.response.DefaultRes;
import com.mscqz.springboot.app.response.ResponseMessage;
import com.mscqz.springboot.app.response.StatusCode;
import com.mscqz.springboot.service.music.AppleMusicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.mscqz.springboot.app.exception.ErrorCode.INVALID_DEVELOPER_TOKEN;

@RequiredArgsConstructor
@RestController
@Slf4j
public class AppleMusicApiController {
    private final AppleMusicService appleMusicService;
    @Value("${apple.token}")
    private String developerToken;

    // Apple Music API 에서 받아온 데이터 확인
    @GetMapping("/api/allow/info/basic")
    public ResponseEntity allowBasic(){
        StringBuilder result = new StringBuilder();
        try{
            // 한국에서 인기 많은 K-Pop 곡 30개 조회
            URL url = new URL("https://api.music.apple.com/v1/catalog/kr/charts?types=songs&genre=51&limit=30");

            // 케이팝 장르 id는 51 체크
            URL url2 = new URL("https://api.music.apple.com/v1/catalog/kr/genres/51");

            // 한국에서 인기 많은 장르 14가지 조회
            URL url3 = new URL("https://api.music.apple.com/v1/catalog/kr/genres");

            // 한국에서 인기 많은 ost 곡 10개 조회
            URL url4 = new URL("https://api.music.apple.com/v1/catalog/kr/charts?types=songs&genre=16&limit=10");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer "+ developerToken);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestMethod("GET");
            BufferedReader rd;

            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300){
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            }
            else{
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line;
            while((line=rd.readLine())!=null){
                result.append(line+"\n");
            }
            rd.close();
            conn.disconnect();
        } catch (Exception e){
            e.printStackTrace();
        }
        log.info("result:{}", result);

        if(result.length() != 0){
            appleMusicService.init(result.toString());
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.GET_MUSICS_FROM_APPLE_MUSIC_API_SUCCESS, result.toString()),HttpStatus.OK);
        }
        else{
            throw new CustomException(INVALID_DEVELOPER_TOKEN);
        }
    }
}
