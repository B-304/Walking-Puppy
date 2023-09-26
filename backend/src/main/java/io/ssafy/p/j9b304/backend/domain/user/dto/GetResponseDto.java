package io.ssafy.p.j9b304.backend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetResponseDto {
    private String nickname;
    private int walkCount;

    @Builder
    public GetResponseDto(String nickname, int walkCount) {
        this.nickname = nickname;
        this.walkCount = walkCount;
    }
}
