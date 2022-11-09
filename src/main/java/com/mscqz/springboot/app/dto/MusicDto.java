package com.mscqz.springboot.app.dto;

import com.mscqz.springboot.domain.music.Music;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MusicDto {
    private String id;
    private String name;
    private String artistName;
    private String artwork;
    private String previews;

    public MusicDto(Music entity){
        this.id = entity.getAppleMusicId();
        this.name = entity.getTitle();
        this.artistName = entity.getArtistName();
        this.artwork = entity.getArtwork();
        this.previews = entity.getPreviews();
    }
}
