package com.toy.projectmate.web.dto.member;

import com.toy.projectmate.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collections;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {

    @NotBlank(message="아이디를 입력해주세요")
    private String studentId;
    @NotBlank(message="이메일을 입력해주세요")
    private String email;

    @NotBlank(message="닉네임을 입력해주세요")
    @Size(min=2, message = "닉네임이 너무 짧습니다")
    private String nickname;

    @NotBlank(message="비밀번호를 입력해주세요")
    /*@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$",
            message = "비밀번호는 8~30 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")*/
    private String password;

    private String checkedPassword;

    private String role;

    /*@Builder
    public Member toEntity(){
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .roles(Collections.singletonList(role))
                .build();
    }*/

}
