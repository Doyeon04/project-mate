package com.toy.projectmate.service;

import com.toy.projectmate.common.CommonResponse;
import com.toy.projectmate.config.security.JwtTokenProvider;
import com.toy.projectmate.domain.member.Member;
import com.toy.projectmate.domain.member.MemberRepository;
import com.toy.projectmate.web.dto.member.SignUpRequestDto;
import com.toy.projectmate.web.dto.member.SignInResultDto;
import com.toy.projectmate.web.dto.member.SignUpResultDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberServiceImpl implements MemberService{

    private final Logger LOGGER = LoggerFactory.getLogger(MemberServiceImpl.class);
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    public final JwtTokenProvider jwtTokenProvider;

    @Override
    public SignUpResultDto signUp(SignUpRequestDto dto) throws Exception{
        LOGGER.info("[getSignUpResult] 회원 가입 정보 전달");

        if(memberRepository.findByStudentId(dto.getStudentId()).isPresent()){
            throw new Exception("이미 존재하는 아이디입니다.");
        }
        if(!dto.getPassword().equals(dto.getCheckedPassword())){
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        Member member;
        if(dto.getRole().equalsIgnoreCase("admin")){
            member = Member.builder()
                    .studentId(dto.getStudentId())
                    .email(dto.getEmail())
                    .nickname(dto.getNickname())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .roles(Collections.singletonList("ROLE_ADMIN"))
                    .build();
        }else{
            member = Member.builder()
                    .studentId(dto.getStudentId())
                    .email(dto.getEmail())
                    .nickname(dto.getNickname())
                    .password(passwordEncoder.encode(dto.getPassword()))
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

        Member member = memberRepository.getByStudentId(id);

        LOGGER.info("[getSignInResult] Id : {}", id);
        LOGGER.info("[getSignInResult] 패스워드 비교 수행");

        if(!passwordEncoder.matches(password, member.getPassword())){ // 패스워드 일치하는지 확인
            throw new RuntimeException("패스워드가 일치하지 않습니다.");
        }
        LOGGER.info("[getSignInResult] 패스워드 일치");
        LOGGER.info("[getSignInResult] SignInResultDto 객체 생성 ");

        SignInResultDto signInResultDto = SignInResultDto.builder()
                .token(jwtTokenProvider.createToken(String.valueOf(member.getStudentId()),
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


}
