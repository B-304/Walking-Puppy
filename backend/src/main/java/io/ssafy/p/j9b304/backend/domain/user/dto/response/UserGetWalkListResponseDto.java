package io.ssafy.p.j9b304.backend.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserGetWalkListResponseDto {
    private Integer walkCount;
    private List<UserGetWalkDetailResponseDto> userWalkList;

    @Builder
    public UserGetWalkListResponseDto(Integer walkCount, List<UserGetWalkDetailResponseDto> userWalkList) {
        this.walkCount = walkCount;
        this.userWalkList = userWalkList;
    }
}
