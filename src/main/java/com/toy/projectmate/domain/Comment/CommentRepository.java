package com.toy.projectmate.domain.Comment;


import com.toy.projectmate.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByMember(Member member);
}
