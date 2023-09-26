package io.ssafy.p.j9b304.backend.domain.walk.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WalkExistPathAddRequestDto {
    @Schema(description = "산책 식별번호")
    Long walkId;
}
