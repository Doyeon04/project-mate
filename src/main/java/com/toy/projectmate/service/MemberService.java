package com.toy.projectmate.service;

import com.toy.projectmate.domain.member.Member;
import com.toy.projectmate.web.dto.member.*;

public interface MemberService {

    SignUpResultDto signUp(SignUpRequestDto requestDto) throws Exception;
    SignInResultDto signIn(String id, String password) throws RuntimeException;

   void modifyPassword(MemberUpdatePasswordDto memberUpdatePasswordDto, Long memberId);

    void modifyNickname(MemberUpdateNicknameDto memberUpdateNicknameDto, Long memberId);

    boolean checkNickname(String nickname);

    boolean checkPassword(Member member, MemberUpdatePasswordDto memberUpdatePasswordDto);
}
