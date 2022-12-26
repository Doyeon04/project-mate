package com.toy.projectmate.web;

import com.toy.projectmate.domain.posts.Posts;
import com.toy.projectmate.domain.posts.PostsRepository;
import com.toy.projectmate.web.dto.posts.PostsDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void clear() throws Exception{
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글_등록_테스트() throws Exception{
        // given
        String title = "title";
        String content = "content";
        PostsDto.Request requestDto = PostsDto.Request.builder()
                .title(title)
                .content(content)
                .writer("writer")
                .subject("subject")
                .division("A")
                .people_num(3)
                .proceed_way(1)
                .is_progress(0)
                .build();

        String url = "http://localhost:"+port+"/posts";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList.get(0).getTitle()).isEqualTo(title);
        assertThat(postsList.get(0).getContent()).isEqualTo(content);
    }

    @Test
    public void 게시글_수정_테스트() throws Exception{
        // given
        Posts savedPosts = postsRepository.save(Posts.builder()
                        .title("title")
                        .content("content")
                        .writer("writername")
                        .subject("subject")
                        .division("N")
                        .people_num(4)
                        .proceed_way(0)
                        .is_progress(0)
                        .build());
        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsDto.Request requestDto = PostsDto.Request.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .writer("writer")
                .subject("subject")
                .division("A")
                .people_num(3)
                .proceed_way(1)
                .is_progress(0)
                .build();

        String url = "http://localhost:"+port+"/posts/"+updateId;

        HttpEntity<PostsDto.Request> requestEntity = new HttpEntity<>(requestDto);

        // when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(postsList.get(0).getContent()).isEqualTo(expectedContent);
    }


}
