package io.ssafy.p.j9b304.backend.domain.user.controller;

import io.ssafy.p.j9b304.backend.domain.security.jwt.JwtToken;
import io.ssafy.p.j9b304.backend.domain.security.oAuth.OauthToken;
import io.ssafy.p.j9b304.backend.domain.user.dto.GetResponseDto;
import io.ssafy.p.j9b304.backend.domain.user.entity.User;
import io.ssafy.p.j9b304.backend.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseBody
    @GetMapping("/oauth/kakao")
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code) {
        // 인가코드로 엑세스 토큰 발급 받기
        OauthToken oauthToken = userService.getAccessToken(code);

        // 엑세스 토큰으로 회원 정보 저장 후 JWT 생성
        User user = userService.saveUser(oauthToken.getAccess_token());

        JwtToken jwtToken = userService.createToken(user);

        return ResponseEntity.ok(jwtToken);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponse(responseCode = "204", description = "회원 탈퇴 성공")
    @Operation(summary = "회원 탈퇴")
    public void userRemove(@PathVariable Long userId) {
        userService.removeUser(userId);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "회원 정보 조회")
    @Operation(summary = "회원 정보 조회", description = "회원 정보 조회를 위한 사용자 식별번호")
    public GetResponseDto userGetDetail(@PathVariable Long userId) {
        return userService.getUserDetail(userId);
    }
}
