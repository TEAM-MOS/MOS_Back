package mos.mosback.stRoom.repository;
import mos.mosback.stRoom.domain.stRoom.StudyMemberTodoEntity;
import mos.mosback.stRoom.domain.stRoom.StudyMemberTodoKey;
import mos.mosback.stRoom.dto.StudyRoomTodoInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface MemberTodoRepository extends JpaRepository<StudyMemberTodoEntity, Long>{

    @Query(value = "SELECT * FROM STUDY_MEMBER_TODO_ENTITY WHERE roomId = :roomId AND memberId = :memberId", nativeQuery = true)
    List<StudyMemberTodoEntity> findAllByStRoom(@Param("roomId") Long roomId, @Param("memberId") Long memberId);
    @Query(value = "SELECT * FROM (\n" +
            "SELECT COUNT(*) AS totalCount FROM STU where roomId = :roomId \n" +
            ") A, (\n" +
            "SELECT COUNT(*) AS completedCount FROM STUDY_MEMBER_TODO_ENTITY where roomId = :roomId AND status = 'Completed'\n" +
            ") B", nativeQuery = true)
    StudyRoomTodoInfoDto getStudyRoomTodoAverage(@Param("roomId") Long roomId);

    Optional<StudyMemberTodoEntity> findByMemberIdAndTodoContent(Long id, String todoContent);
}
