package mos.mosback.stRoom.repository;

import mos.mosback.login.domain.user.User;
import mos.mosback.stRoom.domain.stRoom.StRoomEntity;
import mos.mosback.stRoom.domain.stRoom.StudyMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepository extends JpaRepository<StudyMemberEntity, Long> {

    List<StudyMemberEntity> findAllByMemberId(Long memberId);
    List<StudyMemberEntity> findByStRoom(StRoomEntity stRoom);



}
