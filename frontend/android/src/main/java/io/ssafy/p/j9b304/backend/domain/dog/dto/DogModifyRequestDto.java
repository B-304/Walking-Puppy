package io.ssafy.p.j9b304.backend.domain.dog.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@NoArgsConstructor
@Getter
public class DogModifyRequestDto {

    @NotNull(message = "강아지 이름을 입력해주세요.")
    private String name;


    @Builder
    public DogModifyRequestDto(String name) {
        this.name = name;
    }
}
