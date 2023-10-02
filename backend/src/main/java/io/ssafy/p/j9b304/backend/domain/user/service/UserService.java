package io.ssafy.p.j9b304.backend.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ssafy.p.j9b304.backend.domain.dog.entity.Dog;
import io.ssafy.p.j9b304.backend.domain.dog.service.DogService;
import io.ssafy.p.j9b304.backend.domain.security.jwt.JwtToken;
import io.ssafy.p.j9b304.backend.domain.security.jwt.JwtTokenProvider;
import io.ssafy.p.j9b304.backend.domain.security.oAuth.KakaoProfile;
import io.ssafy.p.j9b304.backend.domain.security.oAuth.OauthToken;
import io.ssafy.p.j9b304.backend.domain.user.dto.request.UserModifyRequestDto;
import io.ssafy.p.j9b304.backend.domain.user.dto.response.UserGetDetailResponseDto;
import io.ssafy.p.j9b304.backend.domain.user.dto.response.UserGetWalkDetailResponseDto;
import io.ssafy.p.j9b304.backend.domain.user.entity.User;
import io.ssafy.p.j9b304.backend.domain.user.repository.UserRepository;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Walk;
import io.ssafy.p.j9b304.backend.domain.walk.repository.WalkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    private final BCryptPasswordEncoder encoder;
    private final WalkRepository walkRepository;

    private final DogService dogService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Autowired
    public UserService(BCryptPasswordEncoder encoder, UserRepository userRepository, AuthenticationManagerBuilder authenticationManagerBuilder, JwtTokenProvider jwtTokenProvider, WalkRepository walkRepository, DogService dogService) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.walkRepository = walkRepository;
        this.dogService = dogService;
    }

    public OauthToken getAccessToken(String code) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);


        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(accessTokenResponse.getBody(), OauthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return oauthToken;

    }

    public User saveUser(String accessToken) {
        KakaoProfile profile = findProfile(accessToken);

        User user = userRepository.findByEmail(profile.getKakao_account().getEmail())
                .orElse(null);


        if (user == null) {
            Dog newDog = dogService.addDog();

            user = User.builder()
                    .kakaoId(profile.getId())
//                    .profileImage(profile.getKakao_account().getProfile().getProfile_image_url())
                    .nickname(profile.getKakao_account().getProfile().getNickname())
                    .email(profile.getKakao_account().getEmail())
                    .dog(newDog)
                    .build();

            userRepository.save(user);

        }
        return user;

    }

    public KakaoProfile findProfile(String token) {

        //(1-2)
        RestTemplate rt = new RestTemplate();

        //(1-3)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token); //(1-4)
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //(1-5)
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        //(1-6)
        // Http 요청 (POST 방식) 후, response 변수에 응답을 받음
        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        //(1-7)
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoProfile;
    }

    public JwtToken createToken(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), null);

        Authentication authentication = jwtTokenProvider.authenticate(authenticationToken);

        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

    public User getUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userCode");

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자가 없습니다."));

        return user;
    }

    public void removeUser(Long userId) {
        User existUser = findUser(userId);

        checkUser(userId, existUser.getUserId());

        existUser.changeState();

        userRepository.deleteById(userId);
    }

    public UserGetDetailResponseDto getUserDetail(Long userId) {
        User existUser = findUser(userId);

        checkUser(userId, existUser.getUserId());

        return userRepository.findByUserId(existUser.getUserId()).get().toDto();
    }

    @Transactional
    public void modifyUser(Long userId, UserModifyRequestDto userModifyRequestDto) {
        User existUser = findUser(userId);

        checkUser(userId, existUser.getUserId());

        existUser.mofidyUser(userModifyRequestDto);
    }

    public User findUser(Long userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    }


    public void checkUser(Long userId, Long originalUserId) {
        if (!userId.equals(originalUserId))
            throw new IllegalArgumentException("사용자가 일치하지 않습니다.");
    }

    public List<UserGetWalkDetailResponseDto> getUserWalkList(HttpServletRequest httpServletRequest) {
        User walker = jwtTokenProvider.extractUserFromToken(httpServletRequest);

        List<Walk> walkList = walkRepository.findByUserAndState(walker, '1');

        return walkList.stream().map(Walk::toUserGetWalkDetailResponseDto).collect(Collectors.toList());
    }
}
