package com.toy.projectmate.domain.posts;

import com.toy.projectmate.domain.BaseTimeEntity;
import com.toy.projectmate.domain.Comment.Comment;
import com.toy.projectmate.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(length = 100, nullable = false)
    private String subject;

    @Column(nullable = false)
    private String division;

    @Column(nullable = false)
    private int people_num;

    @Column(nullable=false)
    private String proceed_way;

    @Column(columnDefinition = "TINYINT", length = 1)
    private int is_progress;

    @Column(columnDefinition = "integer default 0", nullable=false)
    private int view_count;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int bookmark_count;

    @OneToMany(mappedBy = "posts", orphanRemoval = true)
    @Where(clause = "parent_id is null")
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public Posts(String title, Member member, String content, String subject, String division, int people_num, String proceed_way, int is_progress, int view_count) {
        this.title = title;
        this.member = member;
        this.content = content;
        this.subject = subject;
        this.division = division;
        this.people_num = people_num;
        this.proceed_way = proceed_way;
        this.is_progress = is_progress;
        this.view_count = view_count;
    }

    public void update(String title, String content, String subject, String division, int people_num, String proceed_way, int is_progress){
        this.title = title;
        this.content = content;
        this.subject = subject;
        this.division = division;
        this.people_num = people_num;
        this.proceed_way = proceed_way;
        this.is_progress = is_progress;
    }

    public void increaseBookmarkCount(){
        this.bookmark_count += 1;
    }

    public void decreaseBookmarkCount(){
        this.bookmark_count -= 1;
    }

}
