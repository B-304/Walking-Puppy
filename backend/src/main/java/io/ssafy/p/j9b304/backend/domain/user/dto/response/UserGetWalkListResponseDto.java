package io.ssafy.p.j9b304.backend.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserGetWalkListResponseDto {
    private String day;
    private Integer walkCount;
    private boolean isAchieved;
    private List<Long> walkIdList;

    @Builder
    public UserGetWalkListResponseDto(String day, Integer walkCount, boolean isAchieved, List<Long> walkIdList) {
        this.day = day;
        this.walkCount = walkCount;
        this.isAchieved = isAchieved;
        this.walkIdList = walkIdList;
    }
}
