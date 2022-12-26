package com.toy.projectmate.domain.Comment;

import com.toy.projectmate.domain.posts.Posts;
import com.toy.projectmate.domain.posts.PostsRepository;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup(){commentRepository.deleteAll();}


    @Test
    public void 댓글_수정(){
 /*       Posts post = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .writer("writername")
                .subject("subject")
                .division("N")
                .people_num(4)
                .proceed_way(0)
                .is_progress(0)
                .build());

        Comment savedComment = commentRepository.save(Comment.builder()
                .writer("writer")
                .content("content")
                .posts(post)
                .secret(0)
                .parent(null)
                .build());

    */

    }
}