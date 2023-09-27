package io.ssafy.p.j9b304.backend.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserGetDetailResponseDto {
    private String nickname;
    private int walkCount;

    @Builder
    public UserGetDetailResponseDto(String nickname, int walkCount) {
        this.nickname = nickname;
        this.walkCount = walkCount;
    }
}
