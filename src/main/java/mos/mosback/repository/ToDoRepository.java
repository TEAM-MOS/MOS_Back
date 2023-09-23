package mos.mosback.repository;
import mos.mosback.domain.stRoom.ToDoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ToDoRepository extends JpaRepository <ToDoEntity,Long> {
    @Query("SELECT t FROM ToDoEntity t ORDER BY t.TodoId DESC")
    List<mos.mosback.domain.stRoom.ToDoEntity> findAllDesc(); //Todo리스트를 조회시 todoID를 기준으로 내림차순 정렬

    List<ToDoEntity> findByDayOfWeekAndWeekOfYear(String dayOfWeek, int weekOfYear);
}
