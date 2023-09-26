package io.ssafy.p.j9b304.backend.domain.user.entity;

import io.ssafy.p.j9b304.backend.domain.user.dto.GetResponseDto;
import io.ssafy.p.j9b304.backend.domain.user.dto.UserModifyRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
@Table(name = "user")
@SQLDelete(sql = "UPDATE user SET expired_at = now() WHERE user_id = ?")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "kakao_id")
    private Long kakaoId;

    @Column(name = "state")
    private boolean state = false;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "walk_count")
    private int walkCount;

    @Column(name = "email")
    private String email;

    // todo : 강아지
//    @Column(name = "image_id")
//    private String profileImage;

    @Builder
    public User(Long userId, Long kakaoId, boolean state, LocalDateTime createdAt, LocalDateTime expiredAt, String nickname, int walkCount, String email) {
        this.userId = userId;
        this.kakaoId = kakaoId;
        this.state = state;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.nickname = nickname;
        this.walkCount = walkCount;
        this.email = email;
    }

    public User(String email, String nickname) {
    }

    public GetResponseDto toDto() {
        return GetResponseDto.builder()
                .nickname(nickname)
                .walkCount(walkCount)
                .build();
    }

    public void mofidyUser(UserModifyRequestDto userModifyRequestDto) {
        if (StringUtils.hasText(userModifyRequestDto.getNickname()))
            this.nickname = userModifyRequestDto.getNickname();

        if (userModifyRequestDto.getWalkCount() != 0)
            this.walkCount = userModifyRequestDto.getWalkCount();
    }
}
