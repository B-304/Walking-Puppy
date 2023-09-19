package io.ssafy.p.j9b304.backend.domain.walk.controller;

import io.ssafy.p.j9b304.backend.domain.walk.dto.request.WalkAddRequestDto;
import io.ssafy.p.j9b304.backend.domain.walk.service.WalkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
