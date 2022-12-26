package com.toy.projectmate.domain.Comment;

import com.toy.projectmate.domain.BaseTimeEntity;
import com.toy.projectmate.domain.posts.Posts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Posts posts;

    private String writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private Comment parent;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "TINYINT", length = 1)
    private int secret;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public Comment(String writer, String content, int secret, Posts posts, Comment parent) {
        this.writer = writer;
        this.content = content;
        this.secret = secret;
        this.posts = posts;
        this.parent = parent;
    }

    public void update(String content, int secret){
        this.content = content;
        this.secret = secret;
    }
}
