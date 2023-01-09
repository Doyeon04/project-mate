package com.toy.projectmate.domain.bookmark;

import com.toy.projectmate.domain.member.Member;
import com.toy.projectmate.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {


   Optional<Bookmark> findByPostsAndMember(Posts posts, Member member);

   List<Bookmark> findAllByMember(Member member);


}
