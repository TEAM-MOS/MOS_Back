package mos.mosback.repository;

import mos.mosback.domain.stRoom.StudyMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyMemberRepository extends JpaRepository<StudyMemberEntity, Long> {

    List<StudyMemberEntity> findAllByMemberId(Long memberId);

}
