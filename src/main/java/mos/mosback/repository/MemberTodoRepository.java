package mos.mosback.repository;
import mos.mosback.domain.stRoom.StudyMemberTodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberTodoRepository extends JpaRepository <StudyMemberTodoEntity,Long>{

}
