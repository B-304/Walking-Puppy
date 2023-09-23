package io.ssafy.p.j9b304.backend.domain.walk.controller;

import io.ssafy.p.j9b304.backend.domain.walk.dto.request.WalkAddRequestDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.request.WalkExistPathAddRequestDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.request.WalkModifyRequestDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.request.WalkSaveRequestDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.response.WalkGetDetailResponseDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.response.WalkGetListResponseDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.response.WalkInitialInfoResponseDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.response.WalkSaveResponseDto;
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
    public WalkInitialInfoResponseDto walkNewPathAdd(/* User user, */@RequestBody WalkAddRequestDto walkAddRequestDto) {
        // todo : request check validation
        return walkService.addWalkNewPath(/* user, */ walkAddRequestDto);
    }

    @PostMapping("/exist-path")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "산책 저장된 경로 생성 성공")
    @Operation(summary = "산책 저장된 경로 생성", description = "저장된 경로로 산책하기 위한 데이터 저장")
    public WalkInitialInfoResponseDto walkExistPathAdd(/* User user, */@RequestBody WalkExistPathAddRequestDto walkExistPathAddRequestDto) {
        // todo : request check validation
        return walkService.addWalkExistPath(/* user, */ walkExistPathAddRequestDto);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "내 산책 목록 조회 성공")
    @Operation(summary = "내 산책 목록 조회", description = "산책 경로 스크랩과 저장된 산책 경로에서 사용되는 내 산책 목록")
    public List<WalkGetListResponseDto> walkGetList(/* User user */) {
        List<Walk> walkList = walkService.getWalkList(/* user */);

        return walkList.stream()
                .map(w -> new WalkGetListResponseDto(w))
                .collect(Collectors.toList());
    }

    @GetMapping("/{walkId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "내 산책 단건 조회 성공")
    @Operation(summary = "내 산책 단건 조회", description = "산책 경로 스크랩과 저장된 산책 경로에서 사용되는 내 산책 상세 장보")
    public WalkGetDetailResponseDto walkGetDetail(/* User user, */@PathVariable Long walkId) {
        return walkService.getWalkDetail(/* user, */walkId);
    }

    @PatchMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "산책 수정 성공")
    @Operation(summary = "산책 수정", description = "산책로명 수정")
    public void walkModify(/* User user, */@RequestBody WalkModifyRequestDto walkModifyRequestDto) {
        walkService.modifyWalk(/* user, */ walkModifyRequestDto);
    }

    @DeleteMapping("/{walkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponse(responseCode = "204", description = "산책 삭제 성공")
    @Operation(summary = "산책 삭제", description = "스크랩에 저장된 산책 삭제")
    public void walkRemove(/* User user, */@PathVariable Long walkId) {
        walkService.removeWalk(/* user, */ walkId);
    }

    @PostMapping("/over")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "사용자 산책 데이터 저장 성공")
    @Operation(summary = "산책 종료시 기록 조회", description = "산책 종료 시 사용자 산책 데이터를 저장하고 결과를 조회")
    public WalkSaveResponseDto walkSave(/* User user, */@RequestBody WalkSaveRequestDto walkSaveRequestDto) {
        // 산책 종료 시각을 저장
        return walkService.saveWalk(/* user, */ walkSaveRequestDto);
    }

    @PutMapping("/scrap/{walkId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "산책 스크랩 성공")
    @Operation(summary = "산책 스크랩", description = "산책 스크랩 (보관함에 저장)")
    public void walkModify(/* User user, */ @PathVariable Long walkId) {
        walkService.modifyWalkState(/* user, */ walkId);
    }
}
