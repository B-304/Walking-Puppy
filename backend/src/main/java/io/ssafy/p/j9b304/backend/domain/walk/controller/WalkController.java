package io.ssafy.p.j9b304.backend.domain.walk.controller;

import io.ssafy.p.j9b304.backend.domain.walk.dto.request.*;
import io.ssafy.p.j9b304.backend.domain.walk.dto.response.*;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Walk;
import io.ssafy.p.j9b304.backend.domain.walk.service.RouteService;
import io.ssafy.p.j9b304.backend.domain.walk.service.WalkService;
import io.ssafy.p.j9b304.backend.global.entity.File;
import io.ssafy.p.j9b304.backend.global.repository.FileRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/walk")
@Tag(name = "산책", description = "산책 API 문서")
public class WalkController {
    private final WalkService walkService;
    private final RouteService routeService;
    private final FileRepository fileRepository;

    @PostMapping("/new-path")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "산책 새로운 경로 생성 성공")
    @Operation(summary = "산책 새로운 경로 생성", description = "산책 새로운 경로 생성을 위한 사용자 입력 데이터")
    public WalkInitialInfoResponseDto walkNewPathAdd(HttpServletRequest httpServletRequest, @RequestBody WalkAddRequestDto walkAddRequestDto) {
        return walkService.addWalkNewPath(httpServletRequest, walkAddRequestDto);
    }

    @PostMapping("/exist-path")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "산책 저장된 경로 생성 성공")
    @Operation(summary = "산책 저장된 경로 생성", description = "저장된 경로로 산책하기 위한 데이터 저장")
    public WalkInitialInfoResponseDto walkExistPathAdd(HttpServletRequest httpServletRequest, @RequestBody WalkExistPathAddRequestDto walkExistPathAddRequestDto) {
        return walkService.addWalkExistPath(httpServletRequest, walkExistPathAddRequestDto);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "내 산책 목록 조회 성공")
    @Operation(summary = "내 산책 목록 조회", description = "산책 경로 스크랩과 저장된 산책 경로에서 사용되는 내 산책 목록")
    public List<WalkGetListResponseDto> walkGetList(HttpServletRequest httpServletRequest) {
        List<Walk> walkList = walkService.getWalkList(httpServletRequest);

        List<WalkGetListResponseDto> walkGetListResponseDtos = new ArrayList<>();
        for (Walk walk : walkList) {
            WalkGetListResponseDto dto = new WalkGetListResponseDto(walk);
            if (walk.getImageId() != null) {
                File file = fileRepository.findById(walk.getImageId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 파일이 없습니다."));
                dto.setImageUrl(file.getUrl());
            }
            walkGetListResponseDtos.add(dto);
        }
//        return walkList.stream()
//                .map(w -> new WalkGetListResponseDto(w))
//                .collect(Collectors.toList());
        return walkGetListResponseDtos;
    }

    @GetMapping("/{walkId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "내 산책 단건 조회 성공")
    @Operation(summary = "내 산책 단건 조회", description = "산책 경로 스크랩과 저장된 산책 경로에서 사용되는 내 산책 상세 장보")
    public WalkGetDetailResponseDto walkGetDetail(HttpServletRequest httpServletRequest, @PathVariable Long walkId) {
        return walkService.getWalkDetail(httpServletRequest, walkId);
    }

    @PatchMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "산책 수정 성공")
    @Operation(summary = "산책 수정", description = "산책로명 수정")
    public void walkModify(HttpServletRequest httpServletRequest, @RequestBody WalkModifyRequestDto walkModifyRequestDto) {
        walkService.modifyWalk(httpServletRequest, walkModifyRequestDto);
    }

    @DeleteMapping("/{walkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponse(responseCode = "204", description = "산책 삭제 성공")
    @Operation(summary = "산책 삭제", description = "스크랩에 저장된 산책 삭제")
    public void walkRemove(HttpServletRequest httpServletRequest, @PathVariable Long walkId) {
        walkService.removeWalk(httpServletRequest, walkId);
    }

    @PostMapping("/over")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "사용자 산책 데이터 저장 성공")
    @Operation(summary = "산책 종료시 기록 조회", description = "산책 종료 시 사용자 산책 데이터를 저장하고 결과를 조회")
    public WalkSaveResponseDto walkSave(HttpServletRequest httpServletRequest, @RequestBody WalkSaveRequestDto walkSaveRequestDto) {
        // 산책 종료 시각을 저장
        return walkService.saveWalk(httpServletRequest, walkSaveRequestDto);
    }

    @PutMapping("/scrap")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "산책 스크랩 성공")
    @Operation(summary = "산책 스크랩", description = "산책 스크랩 (보관함에 저장)")
    public void walkScrap(HttpServletRequest httpServletRequest, @RequestPart WalkModifyRequestDto walkModifyRequestDto, @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
        walkService.scrapWalk(httpServletRequest, walkModifyRequestDto, multipartFile);
    }

    @GetMapping("/today")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "201", description = "오늘의 산책 기록 조회 성공")
    @Operation(summary = "오늘의 산책 기록 조회")
    public WalkGetTodayResponseDto walkGetToday(HttpServletRequest httpServletRequest) {
        return walkService.getWalkToday(httpServletRequest);
    }


    @GetMapping("/testSafeRoot")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "경로 제공")
    @Operation(summary = "산책 새로운 경로 생성", description = "산책 새로운 경로 생성을 위한 사용자 입력 데이터")
    public List<Point> walkHadoop() {
        List<Point> safeRoute = routeService.getSafeRoute(127.378548, 36.3523388, 60,1);
        System.out.println(safeRoute);
        return safeRoute;

    }
}
