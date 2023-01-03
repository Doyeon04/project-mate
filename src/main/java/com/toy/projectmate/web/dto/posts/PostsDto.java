package com.toy.projectmate.web.dto.posts;

import com.toy.projectmate.domain.Comment.Comment;
import com.toy.projectmate.domain.member.Member;
import com.toy.projectmate.domain.posts.Posts;
import com.toy.projectmate.web.dto.Comment.CommentDto;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class PostsDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        private String title;
        private String content;
        private Member member;
        private String subject;
        private String division;
        private int people_num;
        private String proceed_way;
        private int is_progress;


        public Posts toEntity(){ // dto -> entity
            Posts posts = Posts.builder()
                    .title(title)
                    .member(member)
                    .content(content)
                    .subject(subject)
                    .is_progress(is_progress)
                    .division(division)
                    .people_num(people_num)
                    .proceed_way(proceed_way)
                    .build();

            return posts;
        }
    }
    @Getter
    public static class Response{
        private Long id;
        private String title;
        private String content;
        private String writer_nickname;
        private String writer_id;

        private String subject;
        private String division;
        private int people_num;
        private String proceed_way;
        private int is_progress;
        private String createdDate;
        private String modifiedDate;
        private int view_count;
        private List<CommentDto.Response> commentList;



        public Response(Posts entity){ // entity -> dto
            this.id = entity.getId();
            this.title = entity.getTitle();
            this.writer_nickname = entity.getMember().getNickname();
            this.writer_id = entity.getMember().getStudentId();
            this.content = entity.getContent();
            this.subject = entity.getSubject();
            this.division = entity.getDivision();
            this.people_num = entity.getPeople_num();
            this.proceed_way = entity.getProceed_way();
            this.is_progress=entity.getIs_progress();
            this.createdDate = entity.getCreatedDate();
            this.modifiedDate = entity.getModifiedDate();
            this.view_count = entity.getView_count();
            this.commentList = entity.getCommentList().stream().map(CommentDto.Response::new).collect(Collectors.toList());
        }
    }


}
