package com.toy.projectmate.web.dto.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class MemberUpdatePasswordDto {



    //@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{8,16}", message = "최소 하나의 문자 및 숫자를 포함한 8~16자이여야 합니다")
    @Pattern(regexp="^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$", message="영문자, 숫자, 특수문자 조합 8자 이상이여야 합니다.")
    @NotBlank(message="비밀번호를 입력해주세요")
    private String password;

    @NotBlank(message="비밀번호를 확인해주세요")
    private String checkedPassword;
}
