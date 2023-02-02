package com.toy.projectmate.web.dto.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MemberUpdateNicknameDto {
    @NotBlank(message="닉네임을 입력해주세요")
    @Size(min=2, message = "닉네임이 너무 짧습니다")
    private String nickname;
}
