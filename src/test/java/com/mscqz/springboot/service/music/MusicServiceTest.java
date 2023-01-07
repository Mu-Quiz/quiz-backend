package com.mscqz.springboot.service.music;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class MusicServiceTest {
    private static final int TITLE_LENGTH_LIMIT = 7;
    @Test
    @DisplayName("음악 제목 필터링 기능 체크")
    public void checkMusicTitleFiltering_InfoUpdated(){
        List<String> titleList = new ArrayList<>(Arrays.asList("ㅇㅇㅇ ㅇㅇㅇㅇ ㄴ","ㅀ","17ㅋ","ㅇㅇ","26","12시 30분","Rush Hour (feat. j-hope of BTS)", "그때 그 순간 그대로 (그그그)", "해요 (2022)", "사건의 지평선", "ANTIFRAGILE", "Hype Boy", "자격지심 (feat. ZICO)", "작은 것들을 위한 시 (Boy With Luv) [feat. Halsey]", "낙하 (with 아이유)"));

        for (String title : titleList){
            // 괄호가 있다면 괄호 전까지의 글자 중에서 검사
            if(title.contains("(")){
                title = title.substring(0, title.indexOf("(")-1);
            }
            // 제목에 한글 or 한글자음 or 숫자 or 띄어쓰기만 있고, 띄어쓰기 제외 7자 이내인 타이틀곡인 경우, 퀴즈 리스트에 추가
            if(Pattern.matches("^[ㄱ-힣\\s\\d]*$", title) && title.replaceAll("[^a-zA-Zㄱ-힣\\d]", "").length() <= TITLE_LENGTH_LIMIT){
                log.info("한글 or 한글자음 or 숫자 or 띄어쓰기만 있고, 띄어쓰기 제외 7자 이내인 제목의 음악 : {}", title);
            }
            // 그 외 음악들은 DB에서 삭제(또는 프론트한테 안 보내주는 것도 괜찮을 듯)
            else{
                log.info("삭제 예정 음악 : {}", title);
            }
        }
        System.out.println("시스템 종료");
    }
}
