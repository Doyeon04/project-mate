package com.toy.projectmate.domain.bookmark;


import com.toy.projectmate.domain.member.Member;
import com.toy.projectmate.domain.posts.Posts;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
@Builder
@Entity
public class Bookmark {

    @Id
    @Column(name="bookmark_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id", nullable = false)
    private Posts posts;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Member member;


    public Bookmark(Posts posts, Member member) {
        this.posts = posts;
        this.member = member;
    }

    public void cancelBookmark(Posts posts){
        posts.decreaseBookmarkCount();
    }
}
