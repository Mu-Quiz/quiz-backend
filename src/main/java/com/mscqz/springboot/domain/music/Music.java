package com.mscqz.springboot.domain.music;

import com.mscqz.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@DynamicInsert
@Table(name="MUSIC")
public class Music extends BaseTimeEntity {
    // music 테이블 기본키(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 테이블 칼럼 - Apple Music API ID
    @Column(nullable = false)
    private String appleMusicId;

    // 테이블 칼럼 - 노래 제목
    @Column(nullable = false)
    private String title;

    // 테이블 칼럼 - 아티스트명
    @Column(nullable = false)
    private String artistName;

    // 테이블 칼럼 - 앨범 아트 URL
    @Column(nullable = false)
    private String artwork;

    // 테이블 칼럼 - 미리듣기 URL
    @Column(nullable = false)
    private String previews;

    @Builder
    public Music(String appleMusicId, String title, String artistName, String artwork, String previews){
        this.appleMusicId = appleMusicId;
        this.title = title;
        this.artistName = artistName;
        this.artwork = artwork;
        this.previews = previews;
    }
}
