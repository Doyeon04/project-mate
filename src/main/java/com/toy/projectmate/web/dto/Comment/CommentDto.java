package com.toy.projectmate.web.dto.Comment;

import com.toy.projectmate.domain.Comment.Comment;
import com.toy.projectmate.domain.posts.Posts;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class CommentDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        private String content;
        private String writer;

      //  private Posts posts;
       // private Comment parent;
        private Long parentId;
        private int secret;
        public Comment toEntity(Comment parent, Posts post){
            Comment comment = Comment.builder()
                    .posts(post)
                    .writer(writer)
                    .content(content)
                    .secret(secret)
                    .parent(parent)
                    .build();

            return comment;
        }

    }

    @RequiredArgsConstructor
    @Getter
    public static class Response{
        private Long id;
        private String content;
        private int secret;
        private String writer;
        private List<CommentDto.Response> commentList;
        private String createdDate;
        private String modifiedDate;

        public Response(Comment comment){
            this.id = comment.getId();
            this.content = comment.getContent();
            this.secret = comment.getSecret();
            this.writer = comment.getWriter();
            this.commentList = comment.getCommentList().stream().map(CommentDto.Response::new).collect(Collectors.toList());
            this.createdDate = comment.getCreatedDate();
            this.modifiedDate = comment.getModifiedDate();
        }
    }
   /* @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private List<responseComment> commentList;
    }

    public static class responseComment{
        private Long id;
        private String content;
        private int secret;
        private String writer;
        private List<CommentDto.responseComment> commentList;
    }*/
}
