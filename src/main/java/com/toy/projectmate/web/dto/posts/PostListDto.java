package com.toy.projectmate.web.dto.posts;

import com.toy.projectmate.domain.posts.Posts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data

public class PostListDto {
    private Long id;
    private String title;
    private String content;
    private String subject;
    private String division;
    private String writer;
    private int proceed_way;
    private int is_progress;
    private String createdDate;
    private String modifiedDate;
    private int view_count;

    public PostListDto(Posts posts){
        this.id = posts.getId();
        this.title = posts.getTitle();
        this.content = posts.getContent();
        this.writer = posts.getWriter();
        this.subject = posts.getSubject();
        this.division = posts.getDivision();
        this.is_progress = posts.getIs_progress();
        this.proceed_way = posts.getProceed_way();
        this.createdDate = posts.getCreatedDate();
        this.modifiedDate = posts.getModifiedDate();
        this.view_count = posts.getView_count(); 
    }

}
