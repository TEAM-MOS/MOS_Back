package mos.mosback.domain.posts;

//Entity 클래스와 Entity레파지토리 위치 같아야함
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

//인터페이스 생성 후 JpaRepository<Entity 클래스, PK 타입>를 상속하면 기본적인 CRUD 메소드 자동으로 생성됨
public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();


    List<Posts> findByTitleContaining(String keyword); //키워드를 통해 스터디그룹을 검색 할 수 있다

    @Query(value = "SELECT * FROM posts ORDER BY click_count DESC", nativeQuery = true)
    List<Posts> findPopularPosts(); //클릭수로 인기순 나열
}
//Posts 클래스로 DB를 접근하기 위한 클래스