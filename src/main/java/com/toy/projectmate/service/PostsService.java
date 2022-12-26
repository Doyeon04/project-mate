package com.toy.projectmate.service;

import com.toy.projectmate.domain.posts.Posts;
import com.toy.projectmate.domain.posts.PostsRepository;
import com.toy.projectmate.web.dto.posts.PostsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsDto.Request dto){
        return postsRepository.save(dto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsDto.Request dto){
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        posts.update(dto.getTitle(), dto.getContent(), dto.getSubject(), dto.getDivision(), dto.getPeople_num(), dto.getProceed_way(), dto.getIs_progress());
        return id;
    }

    @Transactional(readOnly = true)
    public PostsDto.Response findById(Long id){
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));

        return new PostsDto.Response(posts);
    }

    @Transactional
    public void delete(Long id){
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        postsRepository.delete(posts);
    }

    @Transactional(readOnly = true)
    public Page<Posts> pageList(Pageable pageable){
        return postsRepository.findAll(pageable);
    }


    @Transactional
    public int updateViewCount(Long id){
        return postsRepository.updateViewCount(id);
    }
}
