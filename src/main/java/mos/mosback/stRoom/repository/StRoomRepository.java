package mos.mosback.stRoom.repository;

//Entity 클래스와 Entity레파지토리 위치 같아야함
import mos.mosback.stRoom.domain.stRoom.StRoomEntity;
import mos.mosback.stRoom.domain.stRoom.StudyMemberEntity;
import mos.mosback.stRoom.dto.Home_RoomResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

//인터페이스 생성 후 JpaRepository<Entity 클래스, PK 타입>를 상속하면 기본적인 CRUD 메소드 자동으로 생성됨
public interface StRoomRepository extends JpaRepository<StRoomEntity, Long> {

    @Query("SELECT new mos.mosback.stRoom.dto.Home_RoomResponseDto(s) FROM StRoomEntity s ORDER BY s.roomId DESC")
    List<Home_RoomResponseDto> findAllDesc();

    @Query("SELECT new mos.mosback.stRoom.dto.Home_RoomResponseDto(s) FROM StRoomEntity s WHERE s.title LIKE %:keyword% OR s.intro LIKE %:keyword%")
    List<Home_RoomResponseDto> findByTitleContaining(@Param("keyword") String keyword);//키워드를 통해 스터디그룹을 검색 할 수 있다


    @Query(value = "SELECT new mos.mosback.stRoom.dto.Home_RoomResponseDto(s)FROM StRoomEntity s ORDER BY s.click DESC")
    List<Home_RoomResponseDto> findPopularRoom();

    @Query("SELECT new mos.mosback.stRoom.dto.Home_RoomResponseDto(s) FROM StRoomEntity s")
    List<Home_RoomResponseDto> findHomeStRoomField();
    @Query("SELECT new mos.mosback.stRoom.dto.Home_RoomResponseDto(s) FROM StRoomEntity s WHERE s.category = :category")
    List<Home_RoomResponseDto> findByCategory(@Param("category") String category);


    @Query("SELECT new mos.mosback.stRoom.dto.Home_RoomResponseDto(s) FROM StRoomEntity s WHERE s.startDate > current_timestamp")
    List<Home_RoomResponseDto> findRecruitingStudies();


    List<StRoomEntity> findByMembersIn(List<StudyMemberEntity> studyMemberships);

    List<StRoomEntity> findByCreatedByUserEmail(String userEmail);

}