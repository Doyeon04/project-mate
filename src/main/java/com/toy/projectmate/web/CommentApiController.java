package com.toy.projectmate.web;

import com.toy.projectmate.service.CommentService;
import com.toy.projectmate.web.dto.Comment.CommentDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/comment")
@RestController
public class CommentApiController {
    private final CommentService commentService;

    @ApiOperation(value="댓글 등록")
    @PostMapping("/{postId}")
    public ResponseEntity saveComment(@PathVariable Long postId, @RequestBody CommentDto.Request requestDto) throws Exception {
        return ResponseEntity.ok( commentService.saveComment(postId, requestDto));
    }

    @ApiOperation(value="댓글 수정")
    @PutMapping("/{commentId}")
    public ResponseEntity updateComment(@PathVariable Long commentId, @RequestBody CommentDto.Request requestDto){
        commentService.updateComment(commentId, requestDto);
        return ResponseEntity.ok(commentId);
    }

    @ApiOperation(value="댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity delete(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(commentId);
    }
}
