package com.mscqz.springboot.domain.music;

import com.mscqz.springboot.app.dto.MusicDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MusicRepository extends JpaRepository<Music, Long> {
    // 전체 음악 조회
    @Query("SELECT m FROM Music m")
    List<MusicDto> findAllDesc();

    // 전체 음악 페이징 처리해서 조회
    @Query("SELECT m FROM Music m")
    Page<MusicDto> findPageAllDesc(Pageable pageable);

    //  Apple Music API ID로 음악 조회
    @Query("SELECT m FROM Music m WHERE appleMusicId = :appleMusicId")
    Optional<Music> findByAppleMusicId(@Param("appleMusicId") String appleMusicId);
}
