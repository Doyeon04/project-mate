package com.toy.projectmate.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //.formLogin().disable()
                .httpBasic().disable()// Bearer 방식 사용 (토큰)
                //.cors().disable()
               .csrf().disable().authorizeRequests()
                //.cors().and()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/member/sign-in", "/member/sign-up", "/member/exception").permitAll()
                .antMatchers("**exception**").permitAll()
                .antMatchers("/post/postList/filtering", "/post/postList").permitAll()
               // .antMatchers("/member").hasRole("USER")
                //.anyRequest().hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity webSecurity){
        webSecurity.ignoring().antMatchers( "/v3/api-docs",  "/swagger-resources/**",   "/swagger-ui.html",   "/webjars/**",  "/swagger/**", "/member/exception");
    }


/*    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/file/**",
            "/image/**",
            "/swagger/**",
            "/swagger-ui/**",
            "/h2/**"
    };*/

    // 정적인 파일 요청에 대해 무시
 /*   @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(AUTH_WHITELIST);
    }*/
}
