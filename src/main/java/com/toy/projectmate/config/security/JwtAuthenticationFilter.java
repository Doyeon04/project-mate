package com.toy.projectmate.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// JWT 토큰으로 인증하고 SecurityContxtHolder에 추가하는 필터를 설정하는 클래스
public class JwtAuthenticationFilter extends OncePerRequestFilter { // 필터를 상속받아 사용

    private final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {

        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       String token = jwtTokenProvider.resolveToken(request); // 토큰 값 추출

      //  String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        LOGGER.info("[doFilterInternal] token 값 추출 완료. token : {}", token);

        LOGGER.info("[doFilterInternal] token 값 유효성 체크 시작");
        if (token != null && jwtTokenProvider.validateToken(token)) {// 토큰 유효성 검사
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            SecurityContextHolder.getContext().setAuthentication(authentication); // 토큰이 유효하면 Authentication 객체를 생성해 SecurityContextHolder에 추가
            LOGGER.info("[doFilterInternal] token 값 유효성 체크 완료");
        }

        filterChain.doFilter(request, response);

    }
}
