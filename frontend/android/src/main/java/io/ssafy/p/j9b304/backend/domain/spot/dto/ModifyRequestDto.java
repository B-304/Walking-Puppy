package io.ssafy.p.j9b304.backend.domain.spot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class ModifyRequestDto {
    @NotNull(message = "이름을 입력해주세요.")
    @Size(min = 1)
    private String name;
    private char open;

}
