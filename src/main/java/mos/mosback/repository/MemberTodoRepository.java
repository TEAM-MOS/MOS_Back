package mos.mosback.repository;
import mos.mosback.domain.stRoom.StudyMemberTodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberTodoRepository extends JpaRepository<StudyMemberTodoEntity, Long>{

}
