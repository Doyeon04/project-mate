package com.toy.projectmate.service;

import com.toy.projectmate.domain.Comment.Comment;
import com.toy.projectmate.domain.Comment.CommentRepository;
import com.toy.projectmate.domain.posts.Posts;
import com.toy.projectmate.domain.posts.PostsRepository;
import com.toy.projectmate.web.dto.Comment.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor

public class CommentService {
    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;

    @Transactional
    public Long saveComment(Long postId, CommentDto.Request requestDto) throws Exception {
        Posts post = postsRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // requestDto.setPosts(post);

        Comment c = null;

        if(!Objects.isNull(requestDto.getParentId())){
            Comment parent = commentRepository.findById(requestDto.getParentId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부모 댓글입니다."));
            if (parent.getParent() != null) { // 부모 댓글은 자신의 부모댓글이 없어야함
                throw new Exception("자식 댓글은 부모 댓글이 될 수 없습니다.");
            }
            if (parent.getPosts().getId() != post.getId()) {
                throw new Exception("해당 게시물에 부모 댓글이 없습니다.");
            }
            c = parent;
        }


       // Comment comment = requestDto.toEntity(parent, post);
        Comment comment = Comment.builder()
                        .posts(post)
                .content(requestDto.getContent())
                .parent(c)
                .secret(requestDto.getSecret())
                .writer(requestDto.getWriter()).build();
        return commentRepository.save(comment).getId();
    }

    @Transactional
    public void updateComment(Long id, CommentDto.Request requestDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        comment.update(requestDto.getContent(), requestDto.getSecret());
    }

    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        commentRepository.delete(comment);
    }
}
