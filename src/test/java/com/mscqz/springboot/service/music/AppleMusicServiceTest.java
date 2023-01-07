package com.mscqz.springboot.service.music;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class AppleMusicServiceTest {
    @Test
    @DisplayName("앨범 아트 사이즈 지정 기능 체크")
    public void setArtworkSize_InfoUpdated() {
        String originArtworkUrl = "https://is2-ssl.mzstatic.com/image/thumb/Music125/v4/35/9f/83/359f83b3-1423-3153-1641-98e948b7fc65/cover_-_EDAM_5_LILAC.jpg/{w}x{h}bb.jpg";
        // {w} -> 1000 으로, {h} -> 1000 으로 변경
        String setArtworkWidthAndHeight = originArtworkUrl.replace("{w}", "1000").replace("{h}", "1000");
        log.info("width : 1000, height : 1000으로 지정한 URL : {}", setArtworkWidthAndHeight);
    }
}
