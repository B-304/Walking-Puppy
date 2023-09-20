package io.ssafy.p.j9b304.backend.domain.walk.controller;

import io.ssafy.p.j9b304.backend.domain.walk.dto.request.WalkAddRequestDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.request.WalkModifyRequestDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.response.WalkGetDetailResponseDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.response.WalkGetListResponseDto;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Walk;
import io.ssafy.p.j9b304.backend.domain.walk.service.WalkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/walk")
@Tag(name = "산책", description = "산책 API 문서")
public class WalkController {
    private final WalkService walkService;

    @PostMapping("/new-path")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "산책 새로운 경로 생성 성공")
    @Operation(summary = "산책 새로운 경로 생성", description = "산책 새로운 경로 생성을 위한 사용자 입력 데이터")
    public void walkNewPathAdd(/* User user, */@RequestBody WalkAddRequestDto walkAddRequestDto) {
        // todo : request check validation
        walkService.addWalkNewPath(/* user, */ walkAddRequestDto);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "내 산책 목록 조회 성공")
    @Operation(summary = "내 산책 목록 조회", description = "산책 경로 스크랩과 저장된 산책 경로에서 사용되는 내 산책 목록")
    public List<WalkGetListResponseDto> walkGetList(/* User user */) {
        List<Walk> walkList = walkService.getWalkList(/* user */);
        List<WalkGetListResponseDto> walkGetListResponseDtoList = walkList.stream()
                .map(w -> new WalkGetListResponseDto(w))
                .collect(Collectors.toList());
        return walkGetListResponseDtoList;
    }

    @GetMapping("/{walkId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "내 산책 목록 조회 성공")
    @Operation(summary = "내 산책 목록 조회", description = "산책 경로 스크랩과 저장된 산책 경로에서 사용되는 내 산책 목록")
    public WalkGetDetailResponseDto walkGetDetail(/* User user, */@PathVariable Long walkId) {
        WalkGetDetailResponseDto walkGetDetailResponseDto = walkService.getWalkDetail(/* user, */walkId);
        return walkGetDetailResponseDto;
    }

    @PatchMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "산책 수정 성공")
    @Operation(summary = "산책 수정", description = "산책로명 수정")
    public void walkModify(/* User user, */@RequestBody WalkModifyRequestDto walkModifyRequestDto) {
        walkService.modifyWalk(/* user, */ walkModifyRequestDto);
    }
}
