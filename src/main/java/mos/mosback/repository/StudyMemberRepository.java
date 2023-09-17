package mos.mosback.repository;

import mos.mosback.domain.posts.StudyMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyMemberRepository extends JpaRepository<StudyMemberEntity, Long> {

}
