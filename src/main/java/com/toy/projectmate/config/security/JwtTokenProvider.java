package com.toy.projectmate.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component // 애플리케이션이 가동되면서 빈으로 자동 주입
public class JwtTokenProvider {

    private final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final UserDetailsService userDetailsService;

   /* @Value("${springboot.jwt.secret}")
    private String secretKey ="secretKey";*/

    @Value("${springboot.jwt.secret}")
    private String secretKey = "secretKey";

    // 토큰 유효시간 168 시간(7일)
    private final long tokenValidTime = 1000L * 60 * 60;


    // 객체 초기화, secretKey 를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        LOGGER.info("[init] JwtTokenProvider 내 secretKey 초기화 시작");
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        LOGGER.info("[init] JwtTokenProvider 내 secretKey 초기화 완료");
    }

    // JWT 토큰 생성
    public String createToken(String userPk, List<String> roles) {
        LOGGER.info("[createToken] 토큰 생성 시작");
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
        claims.put("roles", roles); // 정보는 key/value 쌍으로 저장, 해당 토큰을 사용하는 사용자의 권한 확인할 수 있는 role값
        Date now = new Date();

        // 토큰 생성
        String token = Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘
                // signature 에 들어갈 secret 값 세팅
                .compact();
        LOGGER.info("[createToken] 토큰 생성 완료");
        return token;
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) { // 필터에서 인증이 성공했을 때 SecurityContextHolder에 저장할 Authentication을 생성
        LOGGER.info("[getAuthentication] 토큰 인증 정보 조회 시작");
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        LOGGER.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserDetails UserName : {}", userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUsername(String token) { // secretkey를 설정하고 클레임을 추출해 토큰을 생성할 때 넣었던 sub값을 추출
        LOGGER.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
        String info = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        LOGGER.info("[getUsername] 토큰 기반 회원 정보 추출 완료, info : {}", info);
        return info;
    }

    // Request의 Header에서 token 값을 가져옴. "X-AUTH-TOKEN" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        LOGGER.info("[resolveToken] HTTP 헤더에서 Token 값 추출");

        String authorization = request.getHeader("Authorization");
        if(authorization!= null){
            return authorization.replace("Bearer ", "");
        }
        else return authorization;


    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        LOGGER.info("[validateToken] 토큰 유효 체크 시작");
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            LOGGER.info("[validateToken] 토큰 유효 체크 예외 발생");
            return false;
        }
    }
}
