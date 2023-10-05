package io.ssafy.p.j9b304.backend.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserModifyRequestDto {
    private String nickname;
    private int walkCount;

    @Builder
    public UserModifyRequestDto(String nickname, int walkCount) {
        this.nickname = nickname;
        this.walkCount = walkCount;
    }
}
