package com.toy.projectmate.service;

import com.toy.projectmate.domain.bookmark.Bookmark;
import com.toy.projectmate.domain.bookmark.BookmarkRepository;
import com.toy.projectmate.domain.member.Member;
import com.toy.projectmate.domain.member.MemberRepository;
import com.toy.projectmate.domain.posts.Posts;
import com.toy.projectmate.domain.posts.PostsRepository;
import com.toy.projectmate.web.dto.posts.PostListDto;
import com.toy.projectmate.web.dto.posts.PostsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    public PostsDto.Response findById(Long id, Long memberId){
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        Optional<Posts> optionalPosts = postsRepository.findByPostAndMember(id, member.getId());
        Optional<Bookmark> optionalBookmark = bookmarkRepository.findByPostsAndMember(post, member);

        PostsDto.Response resPost = new PostsDto.Response(post, memberId);
        resPost.setIsWriter(optionalPosts.isPresent());
        resPost.setIsBookmarked(optionalBookmark.isPresent());

        return resPost;
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

        Optional<Bookmark> optionalBookmark = bookmarkRepository.findByPostsAndMember(post, member);

        if(optionalBookmark.isEmpty()){
            // 좋아요를 누른적이 없다면
            post.increaseBookmarkCount();
            Bookmark bookmark = new Bookmark(post, member);
            bookmarkRepository.save(bookmark);
        }else{
            Bookmark bookmark = optionalBookmark.get();
            bookmark.cancelBookmark(post);
            bookmarkRepository.delete(bookmark);
        }

    }

    @Transactional(readOnly = true)
    public Page<PostListDto> findBookmarkedPosts(Member member){

         List<Bookmark> bookmarks = bookmarkRepository.findAllByMember(member);

        List<PostListDto> dtoList = bookmarks.stream()
                .map(bookmark -> new PostListDto(bookmark.getPosts()))
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList);
    }

}
