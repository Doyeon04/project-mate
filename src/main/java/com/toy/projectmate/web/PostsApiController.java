package com.toy.projectmate.web;

import com.toy.projectmate.domain.member.Member;
import com.toy.projectmate.service.PostsService;
import com.toy.projectmate.web.dto.posts.PostListDto;
import com.toy.projectmate.web.dto.posts.PostsDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostsApiController {
    private final PostsService postsService;

    private final Logger logger = LoggerFactory.getLogger(PostsApiController.class);

    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="로그인 성공후 받은 토큰", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value="게시물 등록")
    @PostMapping
    public ResponseEntity save(@RequestBody PostsDto.Request requestDto, @AuthenticationPrincipal Member member){
        return ResponseEntity.ok(postsService.save(requestDto, member.getId()));
    }
    @ApiOperation(value="게시물 수정")
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody PostsDto.Request requestDto, @AuthenticationPrincipal Member member) throws Exception {
        return ResponseEntity.ok(postsService.update(id, requestDto, member.getId()));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name="X-AUTH-TOKEN", value="로그인 성공후 받은 토큰", required = true, dataType = "String", paramType = "header")
    })
    @CrossOrigin(origins="*", allowedHeaders = "*")
    @ApiOperation(value="게시물 조회")
    @GetMapping("/{id}")
    public ResponseEntity read(@PathVariable Long id, @AuthenticationPrincipal Member member
    , HttpServletRequest request, HttpServletResponse response){

        Cookie oldCookie = null; // oldCookie 객체 선언후 빈 값으로 초기화
        Cookie[] cookies = request.getCookies(); // request에서 쿠키들을 가져옴
        if(cookies!=null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("postView")){ // cookie가 있으면 이름이 postview인지 확인하고 맞으면 oldCookie에 대입
                    oldCookie = cookie;
                }
            }
        }
        if(oldCookie!=null){
            if(!oldCookie.getValue().contains("["+ id.toString() +"]")){ // oldCookie의 value중 게시물의 id값이 없을 때 조회수 올리기
                // 있다면 이미 조회한 게시물이므로 조회수 안올라감
                postsService.updateViewCount(id); // view count ++
                // oldcookie에 경로, 쿠키 유지시간 추가해 response에 oldCookie를 전달
                oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24); 							// 쿠키 시간
                response.addCookie(oldCookie);
            }
        }else{ // oldCookie가 null일 때
            postsService.updateViewCount(id);
            Cookie newCookie = new Cookie("postView", "[" + id + "]"); // postView라는 쿠키를 만들고 value는 게시글 아이디로
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24); 								// 쿠키 시간
            response.addCookie(newCookie);
        }

        return ResponseEntity.ok(postsService.findById(id, member.getId()));
    }

    @ApiOperation(value="게시물 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id, @AuthenticationPrincipal Member member) throws Exception {
        postsService.delete(id, member.getId());
        return ResponseEntity.ok(id);
    }

    /*@ApiOperation(value="게시글 전체 조회")
    @GetMapping("/postList")
    public Page<PostListDto> findAllList(@PageableDefault(size=6) Pageable pageable){
        return postsService.pageList(pageable).map(PostListDto::new); // = map(posts -> new PostListDto(posts));
    }
*/
    @ApiOperation(value="게시글 목록 필터링 조회", notes = "과목, 분반, 진행여부에 따라 조회")
    @GetMapping("/postList/filtering")
    public Page<PostListDto> findFilteringList(@PageableDefault(size=6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam String subject, @RequestParam String division, @RequestParam int is_progress){
        return postsService.findFilteringList(pageable, subject, division, is_progress).map(PostListDto::new);
    }

    @ApiOperation(value="게시글 목록 진행여부로 조회")
    @GetMapping("/postList")
    public Page<PostListDto> findListByProgress(@PageableDefault(size=6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam int is_progress){
        return postsService.findListByProgress(pageable, is_progress).map(PostListDto::new);
    }

    @ApiOperation(value = "게시글 북마크 하기")
    @PostMapping("/bookmark/{id}")
    public ResponseEntity bookmarkPost(@ApiParam(value="게시글 id", required = true) @PathVariable Long id, @AuthenticationPrincipal Member member){
        postsService.bookmarkPost(id, member.getId());
        return ResponseEntity.ok(id);
    }



}
