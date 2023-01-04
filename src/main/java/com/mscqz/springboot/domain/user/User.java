package com.mscqz.springboot.domain.user;

import com.mscqz.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "USER", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Email // 이메일 양식이어야 함
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private String userIdentifier; // sub 를 의미

//    @Column(nullable = false)
//    private String idToken; // 그냥 로그인 시 클라이언트 측에서 identity_token 받지 않고 id_token 넘겨주면 됨

    @Builder
    public User(String name, String email, String refreshToken, String userIdentifier){
        this.name = name != null ? name : "익명";
        this.email = email;
        this.refreshToken = refreshToken;
        this.userIdentifier = userIdentifier;
    }

    public User refreshTokenUpdate(String refreshToken){
        this.refreshToken = refreshToken;

        return this;
    }

    public void updateName(String name){
        this.name = name;
    }
}
