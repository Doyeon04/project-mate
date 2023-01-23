package com.toy.projectmate.web.dto.member;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInResultDto extends SignUpResultDto{
    private String token;
    private String nickname;
    private String studentId;
    private String email;
    @Builder
    public SignInResultDto(boolean success, int code, String msg, String token, String nickname, String studentId, String email){
        super(success, code, msg);
        this.token = token;
        this.nickname = nickname;
        this.studentId = studentId;
        this.email = email;
    }
}
