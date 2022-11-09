package com.mscqz.springboot.app;

import com.mscqz.springboot.app.exception.CustomException;
import com.mscqz.springboot.app.response.DefaultRes;
import com.mscqz.springboot.app.response.ResponseMessage;
import com.mscqz.springboot.app.response.StatusCode;
import com.mscqz.springboot.service.music.MusicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import static com.mscqz.springboot.app.exception.ErrorCode.ALL_MUSIC_NOT_FOUND;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MusicApiController {
    private final MusicService musicService;

    // 전체 음악 조회
    @GetMapping("/api/v1/music/all")
    public ResponseEntity getMusicListAll () {
        if(musicService.findAllDesc().size()!=0){
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.GET_ALL_MUSICS, musicService.findAllDesc()),HttpStatus.OK);
        }
        else{
            throw new CustomException(ALL_MUSIC_NOT_FOUND);
        }
    }

    // 전체 음악 10개씩 조회
    @GetMapping("/api/v1/music/paging")
    public ResponseEntity getMusicListWithPaging (@PageableDefault(size=10, sort = "id") Pageable pageable) {
        if(musicService.pageList(pageable).getTotalElements() != 0){
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.GET_ALL_MUSICS_WITH_PAGING, musicService.pageList(pageable)),HttpStatus.OK);
        }
        else{
            throw new CustomException(ALL_MUSIC_NOT_FOUND);
        }
    }
}
