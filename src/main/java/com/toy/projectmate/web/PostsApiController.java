package com.toy.projectmate.web;

import com.toy.projectmate.service.PostsService;
import com.toy.projectmate.web.dto.posts.PostListDto;
import com.toy.projectmate.web.dto.posts.PostsDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostsApiController {
    private final PostsService postsService;

    private final Logger logger = LoggerFactory.getLogger(PostsApiController.class);

    @ApiOperation(value="게시물 등록")
    @PostMapping
    public ResponseEntity save(@RequestBody PostsDto.Request requestDto){
        logger.info("save 메서드 호출");
        return ResponseEntity.ok(postsService.save(requestDto));
    }
    @ApiOperation(value="게시물 수정")
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody PostsDto.Request requestDto){
        return ResponseEntity.ok(postsService.update(id, requestDto));
    }
    @ApiOperation(value="게시물 조회")
    @GetMapping("/{id}")
    public ResponseEntity read(@PathVariable Long id){
        postsService.updateViewCount(id); // view count ++
        return ResponseEntity.ok(postsService.findById(id));
    }

    @ApiOperation(value="게시물 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        postsService.delete(id);
        return ResponseEntity.ok(id);
    }

    @ApiOperation(value="게시글 전체 조회")
    @GetMapping("/postList")
    public Page<PostListDto> list(@PageableDefault(size=6) Pageable pageable){
        return postsService.pageList(pageable).map(PostListDto::new);
    }

}
