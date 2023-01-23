package com.toy.projectmate.web;

import com.toy.projectmate.domain.member.Member;
import com.toy.projectmate.service.MemberService;
import com.toy.projectmate.service.PostsService;
import com.toy.projectmate.web.dto.member.SignUpRequestDto;
import com.toy.projectmate.web.dto.member.SignInResultDto;
import com.toy.projectmate.web.dto.member.SignUpResultDto;
import com.toy.projectmate.web.dto.posts.PostListDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberApiController {

    private final Logger LOGGER = LoggerFactory.getLogger(MemberApiController.class);
    private final MemberService memberService;
    private final PostsService postsService;

    @PostMapping(value = "/sign-in")
    public SignInResultDto signIn(@RequestParam String id, @RequestParam String password) throws RuntimeException {
        LOGGER.info("[signIn] 로그인을 시도하고 있습니다. id : {}, pw : ****", id);
        SignInResultDto signInResultDto = memberService.signIn(id, password);

        if(signInResultDto.getCode() == 0){
            LOGGER.info("[signIn] 정상적으로 로그인되었습니다. id : {}, token : {}", id, signInResultDto.getToken());
        }
        return signInResultDto;
    }

    @PostMapping(value = "/sign-up")
    public SignUpResultDto signUp(
            @Valid @RequestBody SignUpRequestDto requestDto) throws Exception {

        LOGGER.info("[signUp] 회원가입을 수행합니다. id : {}. password : ****, name: {}, role : {}", requestDto.getStudentId(), requestDto.getNickname(), requestDto.getRole());
        SignUpResultDto signUpResultDto = memberService.signUp(requestDto);

        LOGGER.info("[signUp] 회원가입을 완료했습니다. id : {}", requestDto.getStudentId());
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

    @ApiOperation(value = "북마크 한 글 조회", notes="유저가 북마크한 글들 조회")
    @GetMapping("/bookmark-posts")
    public Page<PostListDto> findBookmarkedPostsByMember( @AuthenticationPrincipal Member member){
        return postsService.findBookmarkedPosts(member);
    }

    @ApiOperation(value="작성한 글 조회", notes="유저가 작성한 글들 조회")
    @GetMapping("/posts")
    public Page<PostListDto> findPostsByMember(Pageable pageable, @AuthenticationPrincipal Member member){
        return postsService.findPostsByMember(pageable,member).map(PostListDto::new);
    }

    @ApiOperation(value="작성한 댓글의 글 조회", notes="유저가 작성한 댓글들의 글들 조회")
    @GetMapping("/commented-posts")
    public Page<PostListDto> findCommentedPostsByMember(@AuthenticationPrincipal Member member){
        return postsService.findCommentedPosts(member);
    }


}
