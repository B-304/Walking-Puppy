package io.ssafy.p.j9b304.backend.domain.spot.dto;

import io.ssafy.p.j9b304.backend.domain.spot.entity.Spot;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
public class AddRequestDto {
    @NotNull(message = "이름을 입력해주세요.")
    @Size(min = 1)
    private String name;

    @NotNull(message = "위도를 입력해주세요.")
    private double latitude;

    @NotNull(message = "경도를 입력해주세요.")
    private double longitude;

    private char open;

    // todo : 이미지 업로드

    public Spot toEntity() {
        return Spot.builder()
                .name(name)
                .latitude(latitude)
                .longitude(longitude)
                .open(open)
                .build();
    }
}
