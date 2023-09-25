package io.ssafy.p.j9b304.backend.domain.user.controller;

import io.ssafy.p.j9b304.backend.domain.security.jwt.JwtToken;
import io.ssafy.p.j9b304.backend.domain.security.oAuth.OauthToken;
import io.ssafy.p.j9b304.backend.domain.user.entity.User;
import io.ssafy.p.j9b304.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
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
}
