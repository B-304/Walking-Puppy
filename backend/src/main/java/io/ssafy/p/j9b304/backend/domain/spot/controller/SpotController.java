package io.ssafy.p.j9b304.backend.domain.spot.controller;

import io.ssafy.p.j9b304.backend.domain.spot.dto.SpotAddRequestDto;
import io.ssafy.p.j9b304.backend.domain.spot.dto.SpotModifyRequestDto;
import io.ssafy.p.j9b304.backend.domain.spot.service.SpotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spot")
@RequiredArgsConstructor
@Tag(name = "스팟", description = "스팟 API 모음")
public class SpotController {
    private final SpotService spotService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "스팟 생성 성공")
    @Operation(summary = "스팟 생성", description = "스팟 생성을 위한 사용자 입력 데이터")
    public void dogAdd(@RequestBody SpotAddRequestDto spotAddRequestDto) {
        spotService.addSpot(spotAddRequestDto);
    }

    @PatchMapping("{spotId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponse(responseCode = "201", description = "스팟 수정 성공")
    @Operation(summary = "스팟 수정", description = "스팟 수정을 위한 사용자 입력 데이터")
    public void dogModify(@PathVariable Long spotId, @RequestBody SpotModifyRequestDto spotModifyRequestDto) {
        spotService.modifySpot(spotId, spotModifyRequestDto);
    }
}
