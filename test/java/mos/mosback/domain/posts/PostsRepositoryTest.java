package mos.mosback.domain.posts;
import mos.mosback.domain.posts.Posts;
import mos.mosback.domain.posts.PostsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest

public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @AfterEach //Juit에서 단위 테스트가 끝날 때 마다 수행되는 메서드 지정
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void savePost() {
        //given
        String title = "테스트 제목";
        String goal = "스터디 목표";

        postsRepository.save(Posts.builder() //테이블 posts에 insert/update 쿼리 실행
                // id 값 있으면 update, 없으면 insert 쿼리 실행
                .title(title)
                .goal(goal)

                .username("정히")

                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getGoal()).isEqualTo(goal);
    }
    @Test
    public void BaseTimeEntity_Resister() {
        //given
        LocalDateTime now = LocalDateTime.of(2023, 07, 30, 0,0,0);
        postsRepository.save(Posts.builder().title("title")
                .goal("goal")
                .username("username")

                . build());
        //when
        List<Posts> postsList = postsRepository.findAll();
        //then
        Posts posts = postsList.get(0);
        System.out.println(">>>>>>>>> createDate ="+posts.getCreatedDate()+"," +
                "modifiedDate="+posts.getModifiedDate());
        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }

}
