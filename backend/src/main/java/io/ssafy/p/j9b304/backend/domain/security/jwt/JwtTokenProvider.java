package io.ssafy.p.j9b304.backend.domain.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.ssafy.p.j9b304.backend.domain.user.entity.User;
import io.ssafy.p.j9b304.backend.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, UserRepository userRepository) {
        this.userRepository = userRepository;
        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
        this.key = Keys.hmacShaKeyFor(secretByteKey);
    }

    public JwtToken generateToken(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));


        Claims claims = Jwts.claims().setSubject(String.valueOf(authentication.getCredentials()));
        Date now = new Date();

        //Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .claim("type", "ACCESS")
                .claim("userId", authentication.getCredentials())
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 30))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();


        //Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("type", "ACCESS")
                .claim("userId", authentication.getCredentials())
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 60 * 36))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public Authentication authenticate(Authentication authentication) {
        User findUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 사용자가 없습니다."));

        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (findUser.getExpiredAt() != null) {// 삭제한 날짜가 있다면 => 탈퇴한 회원이거나
            throw new RuntimeException("아이디 또는 비밀번호를 확인하세요");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), findUser.getUserId(), authorities);
    }

    public Authentication getAuthentication(String accessToken) {
        //토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }


        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public User extractUserFromToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader("Authorization");

        String token = bearerToken.substring(7);

        User user = userRepository.findByUserId(Long.parseLong(parseClaims(token).get("userId").toString()))
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));

        return user;
    }
}
