package com.toy.projectmate.service;

import com.toy.projectmate.common.CommonResponse;
import com.toy.projectmate.config.security.JwtTokenProvider;
import com.toy.projectmate.domain.member.Member;
import com.toy.projectmate.domain.member.MemberRepository;
import com.toy.projectmate.web.dto.member.MemberSignUpRequestDto;
import com.toy.projectmate.web.dto.member.SignInResultDto;
import com.toy.projectmate.web.dto.member.SignUpResultDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberServiceImpl implements MemberService{

    private final Logger LOGGER = LoggerFactory.getLogger(MemberServiceImpl.class);
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    public final JwtTokenProvider jwtTokenProvider;

    @Override
    public SignUpResultDto signUp(String id, String password, String name, String role){
        LOGGER.info("[getSignUpResult] 회원 가입 정보 전달");
        Member member;
        if(role.equalsIgnoreCase("admin")){
            member = Member.builder()
                    .email(id)
                    .nickname(name)
                    .password(passwordEncoder.encode(password))
                    .roles(Collections.singletonList("ROLE_ADMIN"))
                    .build();
        }else{
            member = Member.builder()
                    .email(id)
                    .nickname(name)
                    .password(passwordEncoder.encode(password))
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
        }

        Member savedMember = memberRepository.save(member);
        SignUpResultDto signUpResultDto = new SignInResultDto();
        LOGGER.info("[getSignUpResult] userEntity 값이 들어왔는지 확인 후 결과값 주입");
        if(!savedMember.getNickname().isEmpty()){
            LOGGER.info("[getSignUpResult] 정상 처리 완료");
            setSuccessResult(signUpResultDto);
        }else{
            LOGGER.info("[getSignUpResult] 실패 처리 완료");
            setFailResult(signUpResultDto);
        }
        return signUpResultDto;
    }

    public SignInResultDto signIn(String id, String password) throws RuntimeException{
        LOGGER.info("[getSignInResult] signDataHandler로 회원 정보 요청");

        Member member = memberRepository.getByEmail(id);

        LOGGER.info("[getSignInResult] Id : {}", id);
        LOGGER.info("[getSignInResult] 패스워드 비교 수행");

        if(!passwordEncoder.matches(password, member.getPassword())){ // 패스워드 일치하는지 확인
            throw new RuntimeException();
        }
        LOGGER.info("[getSignInResult] 패스워드 일치");
        LOGGER.info("[getSignInResult] SignInResultDto 객체 생성 ");

        SignInResultDto signInResultDto = SignInResultDto.builder()
                .token(jwtTokenProvider.createToken(String.valueOf(member.getEmail()),
                        member.getRoles()))
                .build();
        LOGGER.info("[getSignInResult] SignInResultDto 객체에 값 주입");
        setSuccessResult(signInResultDto);

        return signInResultDto;
    }

    private void setSuccessResult(SignUpResultDto result){

        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFailResult(SignUpResultDto result){
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
    }



   /* @Transactional
    @Override
    public Long signUp(MemberSignUpRequestDto requestDto) throws Exception{
        if(memberRepository.findByEmail(requestDto.getEmail()).isPresent()){
            throw new Exception("이미 존재하는 이메일입니다.");
        }
        if(!requestDto.getPassword().equals(requestDto.getCheckedPassword())){
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }
        Member member = memberRepository.save(requestDto.toEntity());
        member.encodePassword(passwordEncoder);

        return member.getId();
    }

    @Override
    public String login(Map<String, String> members){
        Member member = memberRepository.findByEmail(members.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 Email입니다."));

        String password = members.get("password");
        if(!passwordEncoder.matches(password, member.getPassword())){
            throw new RuntimeException("잘못된 비밀번호입니다.");
        }

        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }*/
}
