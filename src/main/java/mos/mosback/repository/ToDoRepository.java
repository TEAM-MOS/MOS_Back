package mos.mosback.repository;
import mos.mosback.domain.stRoom.ToDoEntity;
import mos.mosback.stRoom.dto.ToDoDateDto;
import mos.mosback.stRoom.dto.ToDoContentResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ToDoRepository extends JpaRepository <ToDoEntity,Long> {
    @Query("SELECT t FROM ToDoEntity t ORDER BY t.TodoId DESC")
    List<ToDoEntity> findAllDesc(); //Todo리스트를 조회시 todoID를 기준으로 내림차순 정렬

    List<ToDoEntity> findByDayOfWeekAndWeekOfYear(String dayOfWeek, int weekOfYear);


    @Query("SELECT new mos.mosback.stRoom.dto.ToDoContentResponseDto(t) FROM ToDoEntity t")
     List<ToDoContentResponseDto>findByDate(int year, int month, int weekOfYear, String dayOfWeek);

    @Query("SELECT new mos.mosback.stRoom.dto.ToDoDateDto(t) FROM ToDoEntity t")
    List<ToDoDateDto>getTodoDate();
}
