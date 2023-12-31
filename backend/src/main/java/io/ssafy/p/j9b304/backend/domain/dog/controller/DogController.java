package io.ssafy.p.j9b304.backend.domain.dog.controller;

import io.ssafy.p.j9b304.backend.domain.dog.dto.DogGetResponseDto;
import io.ssafy.p.j9b304.backend.domain.dog.dto.DogModifyRequestDto;
import io.ssafy.p.j9b304.backend.domain.dog.service.DogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dog")
//todo : swagger dependency 추가
public class DogController {
    private final DogService dogService;

    @PostMapping("/{dogId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "강아지 이름 수정 성공")
    @Operation(summary = "강아지 이름 수정")
    public void dogModify(HttpServletRequest httpServletRequest, @PathVariable Long dogId, @RequestBody DogModifyRequestDto dogModifyRequestDto) {

        dogService.modifyDog(httpServletRequest, dogId, dogModifyRequestDto);
    }

    @GetMapping("/{dogId}")
    @ApiResponse(responseCode = "200", description = "강아지 단건 조회")
    public DogGetResponseDto dogGet(@PathVariable Long dogId) {
        return dogService.getDog(dogId);
    }

}
