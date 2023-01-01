package com.toy.projectmate.service;

import com.toy.projectmate.web.dto.member.MemberSignUpRequestDto;
import com.toy.projectmate.web.dto.member.SignInResultDto;
import com.toy.projectmate.web.dto.member.SignUpResultDto;

import java.util.Map;

public interface MemberService {
   // public Long signUp(MemberSignUpRequestDto requestDto) throws Exception;

   // public String login(Map<String, String> members);

    SignUpResultDto signUp(String id, String password, String name, String role);
    SignInResultDto signIn(String id, String password) throws RuntimeException;


}
