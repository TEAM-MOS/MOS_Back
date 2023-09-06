package mos.mosback.repository;


import mos.mosback.data.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
    // Custom query methods, if needed
}
