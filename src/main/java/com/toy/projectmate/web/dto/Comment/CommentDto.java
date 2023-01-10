package com.toy.projectmate.web.dto.Comment;

import com.toy.projectmate.domain.Comment.Comment;
import com.toy.projectmate.domain.member.Member;
import com.toy.projectmate.domain.posts.Posts;
import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommentDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        private String content;

        private Long parentId;
        private int secret;

        public Comment toEntity(Comment parent, Posts post){
            Comment comment = Comment.builder()
                    .posts(post)
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
        private String writer_nickname;
        private String writer_id;
        private List<CommentDto.Response> commentList;
        private String createdDate;
        private String modifiedDate;
        private Boolean isWriter;

        public Response(Comment comment, Long currentMemberId, Long postWriterId){
            this.id = comment.getId();

            this.secret = comment.getSecret();
            this.writer_nickname = comment.getMember().getNickname();
            this.writer_id = comment.getMember().getStudentId();
            this.commentList = comment.getCommentList().stream().map(c -> new CommentDto.Response(comment, currentMemberId, postWriterId)).collect(Collectors.toList());
            this.createdDate = comment.getCreatedDate();
            this.modifiedDate = comment.getModifiedDate();

            if(Objects.equals(currentMemberId, comment.getMember().getId())) this.isWriter = true; // 현재 사용자가 댓글 작성자면
            else this.isWriter = false;

            if(comment.getSecret()==1){ // 비밀 댓글이면
                if(Objects.equals(currentMemberId, postWriterId) || Objects.equals(currentMemberId, comment.getMember().getId())){
                    // 현재 사용자가 글 작성자거나 해당 댓글 작성자면 비밀댓글이여도 보이게
                    this.content = comment.getContent();
                }
                else this.content = "비밀 댓글입니다.";
            }else{ // 비밀 댓글 아니면 모두가 다 볼 수 있음
                this.content = comment.getContent();
            }

        }

    }

}
