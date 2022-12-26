package com.toy.projectmate.domain.posts;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE_TIME;


@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup(){
        postsRepository.deleteAll();
    }

    @Test
    public void paging(){
        // given
        for(int i=0; i<10; i++){
            postsRepository.save(Posts.builder()
                    .title("타이틀"+i)
                    .content("내용")
                    .writer("writername")
                    .subject("subject")
                    .division("N")
                    .people_num(4)
                    .proceed_way(0)
                    .is_progress(0)
                    .build());
        }

        // when
        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<Posts> page = postsRepository.findAll(pageRequest);

        // then
        List<Posts> list = page.getContent();
        assertThat(list.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(10);
        assertThat(page.getNumber()).isEqualTo(0); // 페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(4); //전체 페이지 번호
        assertThat(page.isFirst()).isTrue(); // 첫번째 항목인지
        assertThat(page.hasNext()).isTrue(); // 다음 페이지가 있는지

    }
    @Test
    public void 게시글저장_불러오기(){
        LocalDateTime now = LocalDateTime.of(2022,4,2,0,0,0);

        String title = "테스트 제목";
        String content = "테스트 본문";
        //given
        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .writer("writername")
                .subject("subject")
                .division("N")
                .people_num(4)
                .proceed_way(0)
                .is_progress(0)
                .build());
        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);

        System.out.println(">>> creatDate = "+posts.getCreatedDate()+", modifiedDate = "+ posts.getModifiedDate());
        // assertThat(posts.getCreatedDate()).isAfter(now);
    }
}
