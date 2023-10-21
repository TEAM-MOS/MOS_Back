package mos.mosback.repository;
import mos.mosback.domain.stRoom.StudyMemberTodoEntity;
import mos.mosback.domain.stRoom.StudyMemberTodoKey;
import mos.mosback.stRoom.dto.StudyRoomTodoInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MemberTodoRepository extends JpaRepository<StudyMemberTodoEntity, StudyMemberTodoKey>{

    @Query(value = "SELECT * FROM STUDY_MEMBER_TODO_ENTITY WHERE roomID = :roomID AND memberId = :memberId", nativeQuery = true)
    List<StudyMemberTodoEntity> findAllByStRoom(@Param("roomId") Long roomID, @Param("memberId") Long memberId);

    @Query(value = "SELECT * FROM (\n" +
            "SELECT COUNT(*) AS totalCount FROM STUDY_MEMBER_TODO_ENTITY where roomID = :roomId \n" +
            ") A, (\n" +
            "SELECT COUNT(*) AS completedCount FROM STUDY_MEMBER_TODO_ENTITY where roomID = :roomId AND status = 'Completed'\n" +
            ") B", nativeQuery = true)
    StudyRoomTodoInfoDto getStudyRoomTodoAverage(@Param("roomId") Long roomId);
}
