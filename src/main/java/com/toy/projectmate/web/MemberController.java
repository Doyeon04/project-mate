package com.toy.projectmate.web;

import com.toy.projectmate.domain.member.MemberRepository;
import com.toy.projectmate.service.MemberService;
import com.toy.projectmate.web.dto.member.MemberSignUpRequestDto;
import com.toy.projectmate.web.dto.member.SignInResultDto;
import com.toy.projectmate.web.dto.member.SignUpResultDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final Logger LOGGER = LoggerFactory.getLogger(MemberController.class);
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    /*@PostMapping("/join")
    public ResponseEntity join(@Valid @RequestBody MemberSignUpRequestDto requestDto) throws Exception{
        return ResponseEntity.ok(memberService.signUp(requestDto));
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> member){
        return memberService.login(member);
    }*/

    @PostMapping(value = "/sign-in")
    public SignInResultDto signIn(@RequestParam String id, @RequestParam String password) throws RuntimeException {
        LOGGER.info("[signIn] 로그인을 시도하고 있습니다. id : {}, pw : ****", id);
        SignInResultDto signInResultDto = memberService.signIn(id, password);

      /*  if(signInResultDto.getCode() == 0){
            signInResultDto.getToken();
        }*/
        if(signInResultDto.getCode() == 0){
            LOGGER.info("[signIn] 정상적으로 로그인되었습니다. id : {}, token : {}", id, signInResultDto.getToken());
        }
        return signInResultDto;
    }

    @PostMapping(value = "/sign-up")
    public SignUpResultDto signUp(
            @RequestParam String id, @RequestParam String password, @RequestParam String name, @RequestParam String role) {

        LOGGER.info("[signUp] 회원가입을 수행합니다. id : {}. password : ****, name: {}, role : {}", id, name, role);
        SignUpResultDto signUpResultDto = memberService.signUp(id, password, name, role);

        LOGGER.info("[signUp] 회원가입을 완료했습니다. id : {}", id);
        return signUpResultDto;
    }

    @GetMapping(value = "/exception")
    public void exceptionTest() throws RuntimeException {
        throw new RuntimeException("접근이 금지되었습니다.");
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, String>> ExceptionHandler(RuntimeException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        LOGGER.error("ExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "에러 발생");

        return new ResponseEntity<>(map, responseHeaders, httpStatus);
    }
}
