package com.mscqz.springboot.service.music;

import com.mscqz.springboot.app.dto.MusicDto;
import com.mscqz.springboot.domain.music.MusicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MusicService {
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
}
