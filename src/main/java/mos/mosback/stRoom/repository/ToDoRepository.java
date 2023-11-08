package mos.mosback.stRoom.repository;
import mos.mosback.stRoom.domain.stRoom.StudyMemberTodoEntity;
import mos.mosback.stRoom.domain.stRoom.ToDoEntity;
import mos.mosback.stRoom.dto.StRoomToDoResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ToDoRepository extends JpaRepository <ToDoEntity,Long> {
    @Query("SELECT t FROM ToDoEntity t ORDER BY t.todoId")
    List<ToDoEntity> findAllDesc();

    @Query("SELECT NEW mos.mosback.stRoom.dto.StRoomToDoResponseDto(t) FROM ToDoEntity t WHERE t.stRoom.roomId= :roomId ORDER BY t.todoId")
    List<StRoomToDoResponseDto> findByStRoomId(@Param("roomId") Long roomId);


}
