package mos.mosback.domain.posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ToDoRepository extends JpaRepository <ToDoEntity,Long> {
    @Query("SELECT t FROM ToDoEntity t ORDER BY t.TodoId DESC")
    List<ToDoEntity> findAllDesc(); //Todo리스트를 조회시 todoID를 기준으로 내림차순 정렬
    //

}
