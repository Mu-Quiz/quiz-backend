package com.mscqz.springboot.service.music;

import com.mscqz.springboot.app.dto.MusicDto;
import com.mscqz.springboot.domain.music.Music;
import com.mscqz.springboot.domain.music.MusicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MusicService {
    private static final int TITLE_LENGTH_LIMIT = 7;
    private final MusicRepository musicRepository;

    // 전체 음악 조회
    @Transactional(readOnly = true)
    public List<MusicDto> findAllDesc() {
        return musicRepository.findAllDesc();
    }

    // 전체 음악 10개씩 페이징 처리해서 조회
    @Transactional(readOnly = true)
    public Page<MusicDto> pageList(Pageable pageable){
        return musicRepository.findPageAllDesc(pageable);
    }

    // 퀴즈로 쓸 수 있는 음악 필터링
    @Transactional
    public void musicTitleFiltering() {
        List<Music> musicList = musicRepository.findAll();
        for (Music music : musicList){
            // 제목에 괄호가 있다면 괄호 이후는 제외하여 DB에 저장
            if(music.getTitle().contains("(")){
                String excludeAfterBracket = music.getTitle().substring(0, music.getTitle().indexOf("(")-1);
                music.titleUpdate(excludeAfterBracket);
            }
            // 한글, 한글 자음, 숫자, 띄어쓰기 이외에 다른 글자가 있는 제목 OR 띄어쓰기 제외 7자 초과 제목의 음악일 경우, 삭제
            if(!Pattern.matches("^[ㄱ-힣\\s\\d]*$", music.getTitle()) || music.getTitle().replaceAll("[^a-zA-Zㄱ-힣\\d]", "").length() > TITLE_LENGTH_LIMIT){
                log.info("삭제예정 음악 : {}", music.getTitle());
                musicRepository.delete(music);
            }
        }
    }
}
