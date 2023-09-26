package io.ssafy.p.j9b304.backend.domain.spot.controller;

import io.ssafy.p.j9b304.backend.domain.spot.dto.AddRequestDto;
import io.ssafy.p.j9b304.backend.domain.spot.dto.GetHotSpotResponseDto;
import io.ssafy.p.j9b304.backend.domain.spot.dto.GetResponseDto;
import io.ssafy.p.j9b304.backend.domain.spot.dto.ModifyRequestDto;
import io.ssafy.p.j9b304.backend.domain.spot.service.SpotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public void spotAdd(HttpServletRequest httpServletRequest, @RequestBody AddRequestDto addRequestDto) {
        spotService.addSpot(httpServletRequest, addRequestDto);
    }

    @PatchMapping("/{spotId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponse(responseCode = "201", description = "스팟 수정 성공")
    @Operation(summary = "스팟 수정", description = "스팟 수정을 위한 사용자 입력 데이터(스팟명, 공개여부)")
    public void spotModify(HttpServletRequest httpServletRequest, @PathVariable Long spotId, @RequestBody ModifyRequestDto modifyRequestDto) {
        spotService.modifySpot(httpServletRequest, spotId, modifyRequestDto);
    }

    @DeleteMapping("/{spotId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponse(responseCode = "204", description = "스팟 삭제 성공")
    @Operation(summary = "스팟 삭제")
    public void spotRemove(HttpServletRequest httpServletRequest, @PathVariable Long spotId) {
        spotService.removeSpot(httpServletRequest, spotId);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "스팟 목록 조회 성공")
    @Operation(summary = "스팟 목록 조회")
    public List<GetResponseDto> spotGetList(HttpServletRequest httpServletRequest) {
        return spotService.getSpotList(httpServletRequest);
    }

    @GetMapping("/{spotId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "스팟 단건 조회 성공")
    @Operation(summary = "스팟 단건 조회")
    public GetResponseDto spotGetDetail(HttpServletRequest httpServletRequest, @PathVariable Long spotId) {
        return spotService.getSpotDetail(httpServletRequest, spotId);
    }

    @GetMapping("/hot")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "인기스팟 목록 조회 성공")
    @Operation(summary = "인기스팟 목록 조회")
    public List<GetHotSpotResponseDto> hotSpotGetList() {
        return spotService.getHotSpotList();
    }

    @GetMapping("/hot/{spotId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "인기스팟 단건 조회 성공")
    @Operation(summary = "인기스팟 단건 조회")
    public GetHotSpotResponseDto hotSpotGetDetail(@PathVariable Long spotId) {
        return spotService.getHotSpotDetail(spotId);
    }
}
