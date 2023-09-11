package mos.mosback.repository;

//Entity 클래스와 Entity레파지토리 위치 같아야함
import mos.mosback.domain.posts.StRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

//인터페이스 생성 후 JpaRepository<Entity 클래스, PK 타입>를 상속하면 기본적인 CRUD 메소드 자동으로 생성됨
public interface StRoomRepository extends JpaRepository<StRoomEntity, Long> {

    @Query("SELECT s FROM StRoomEntity s ORDER BY s.roomID DESC")
    List<StRoomEntity> findAllDesc();

    List<StRoomEntity> findByTitleContaining(String keyword); //키워드를 통해 스터디그룹을 검색 할 수 있다

    @Query(value = "SELECT * FROM stroomEntity ORDER BY click DESC", nativeQuery = true)
    List<StRoomEntity> findPopularstrooms(); //클릭수로 인기순 나열


}
//strooms 클래스로 DB를 접근하기 위한 클래스