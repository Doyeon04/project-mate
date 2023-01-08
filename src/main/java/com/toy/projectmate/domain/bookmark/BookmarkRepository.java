package com.toy.projectmate.domain.bookmark;

import com.toy.projectmate.domain.member.Member;
import com.toy.projectmate.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Bookmark findByPostsAndMember(Posts posts, Member member);
    //List<Bookmark> findAllByMember(Member member);
}
