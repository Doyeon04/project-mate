package com.toy.projectmate.domain.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    @Override
    Page<Posts> findAll(Pageable pageable);

    @Modifying
    @Query("update Posts p set p.view_count = p.view_count+1 where p.id =:id")
    int updateViewCount(@Param("id") Long id);

    @Query(value="select p from Posts p where p.subject = :subject and p.division = :division and p.is_progress = :is_progress")
    Page<Posts> findAllByFiltering(Pageable pageable, String subject, String division, int is_progress);

    @Query(value="select p from Posts p where p.is_progress = :is_progress")
    Page<Posts> findAllByProgress(Pageable pageable, int is_progress);
}
