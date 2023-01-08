package com.toy.projectmate.service;

import com.toy.projectmate.domain.bookmark.Bookmark;
import com.toy.projectmate.domain.bookmark.BookmarkRepository;
import com.toy.projectmate.domain.member.Member;
import com.toy.projectmate.domain.member.MemberRepository;
import com.toy.projectmate.domain.posts.Posts;
import com.toy.projectmate.domain.posts.PostsRepository;
import com.toy.projectmate.web.dto.posts.PostsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public Long save(PostsDto.Request dto, Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        dto.setMember(member); // 유저 정보 담기
        return postsRepository.save(dto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsDto.Request dto, Long memberId) throws Exception {
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        if(!post.getMember().getId().equals(memberId)){
            throw new Exception("해당 게시글 작성자가 아닙니다.");
        }
        post.update(dto.getTitle(), dto.getContent(), dto.getSubject(), dto.getDivision(), dto.getPeople_num(), dto.getProceed_way(), dto.getIs_progress());
        return id;
    }

    @Transactional(readOnly = true)
    public PostsDto.Response findById(Long id){
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));

        return new PostsDto.Response(posts);
    }

    @Transactional
    public void delete(Long id, Long memberId) throws Exception{
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        if(!post.getMember().getId().equals(memberId)){
            throw new Exception("해당 게시글 작성자가 아닙니다.");
        }

        postsRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public Page<Posts> pageList(Pageable pageable){
        return postsRepository.findAll(pageable);
    }


    @Transactional
    public int updateViewCount(Long id){
        return postsRepository.updateViewCount(id);
    }

    @Transactional
    public Page<Posts> findFilteringList(Pageable pageable, String subject, String division, int is_progress){
        return postsRepository.findAllByFiltering(pageable, subject, division, is_progress);
    }

    @Transactional
    public Page<Posts> findListByProgress(Pageable pageable, int is_progress){
        return postsRepository.findAllByProgress(pageable, is_progress);
    }

    @Transactional
    public void bookmarkPost(Long id, Long memberId){
        Posts post = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다."));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));

        if(bookmarkRepository.findByPostsAndMember(post, member)==null){
            // 좋아요를 누른적이 없다면
            post.increaseBookmarkCount(); // cnt 1증가
            Bookmark bookmark = new Bookmark(post, member); // true 처리
            bookmarkRepository.save(bookmark);
        }else{
            Bookmark bookmark = bookmarkRepository.findByPostsAndMember(post, member);
            bookmark.cancelBookmark(post); // cnt 1감소
            bookmarkRepository.delete(bookmark);
        }

    }

    @Transactional(readOnly = true)
    public Page<Posts> findBookmarkedPosts(Pageable pageable, Member member){
         return postsRepository.findAllByMember(pageable, member);
     /*    List<PostsDto> dtoList = bookmarks.stream()
                 .map(bookmark -> new PostsDto.Response(bookmark.getPosts()))
                 .collect(Collectors.toList());*/

    }

}
