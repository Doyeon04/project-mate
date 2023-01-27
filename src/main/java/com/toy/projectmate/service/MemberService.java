package com.toy.projectmate.service;

import com.toy.projectmate.domain.member.Member;
import com.toy.projectmate.domain.posts.Posts;
import com.toy.projectmate.web.dto.member.MemberUpdateDto;
import com.toy.projectmate.web.dto.member.SignUpRequestDto;
import com.toy.projectmate.web.dto.member.SignInResultDto;
import com.toy.projectmate.web.dto.member.SignUpResultDto;

public interface MemberService {

    SignUpResultDto signUp(SignUpRequestDto requestDto) throws Exception;
    SignInResultDto signIn(String id, String password) throws RuntimeException;

    void modify(MemberUpdateDto memberUpdateDto, Long memberId);


}
