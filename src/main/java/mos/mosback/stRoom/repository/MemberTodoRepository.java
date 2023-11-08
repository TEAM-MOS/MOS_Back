package mos.mosback.stRoom.repository;
import mos.mosback.stRoom.domain.stRoom.MemberTodoRankProjection;
import mos.mosback.stRoom.domain.stRoom.StRoomTodoFindProjection;
import mos.mosback.stRoom.domain.stRoom.StRoomTodoInfoProjection;
import mos.mosback.stRoom.domain.stRoom.StudyMemberTodoEntity;
import mos.mosback.stRoom.dto.StudyRoomTodoInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface MemberTodoRepository extends JpaRepository<StudyMemberTodoEntity, Long>{

    @Query(value = "SELECT * FROM STUDY_MEMBER_TODO_ENTITY WHERE room_id = :roomId AND memberID = :memberID", nativeQuery = true)
    List<StudyMemberTodoEntity> findAllByStRoom(@Param("roomId") Long roomId, @Param("memberID") Long memberID);
    @Query(value = "SELECT * FROM (\n" +
            "SELECT COUNT(*) AS totalCount FROM STUDY_MEMBER_TODO_ENTITY where room_id = :roomId \n" +
            ") A, (\n" +
            "SELECT COUNT(*) AS completedCount FROM STUDY_MEMBER_TODO_ENTITY where room_id = :roomId AND status = 'Completed'\n" +
            ") B", nativeQuery = true)
    List<StRoomTodoInfoProjection> getStudyRoomTodoAverage(@Param("roomId") Long roomId);

    @Query(value = "SELECT MEMBERID AS memberId, ROUND((SUM(CASE WHEN STATUS = 'Completed' THEN 1.0 ELSE 0.0 END) / COUNT(*)) * 100) AS progress " +
            "FROM STUDY_MEMBER_TODO_ENTITY " +
            "WHERE STATUS IN ('Completed', 'Waiting') AND ROOM_ID = :roomId " +
            "GROUP BY MEMBERID ORDER BY progress DESC", nativeQuery = true)

    List<MemberTodoRankProjection> getRankByStRoom(@Param("roomId") Long roomId);

        @Query(value = "SELECT IDX, STATUS, TODO_CONTENT AS todoContent FROM STUDY_MEMBER_TODO_ENTITY " +
                "WHERE DATE = :date " +
                "AND MEMBERID = :memberId " +
                "AND ROOM_ID = :roomId",
                nativeQuery = true)
     List<StRoomTodoFindProjection> findTodoByDateAndMemberIdAndRoomId(
                @Param("date") LocalDate date,
                @Param("memberId") Long memberId,
                @Param("roomId") Long roomId);


}
