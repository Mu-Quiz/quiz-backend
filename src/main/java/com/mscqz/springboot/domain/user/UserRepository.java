package com.mscqz.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // 이미 생성된 사용자인지, 처음 가입하는 사용자인지 판단하기 위한 메소드

    @Query("SELECT u FROM User u ORDER BY u.id DESC")
    List<User> findAllDesc();
}
