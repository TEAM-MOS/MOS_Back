package mos.mosback.stRoom.repository;

import mos.mosback.stRoom.domain.stRoom.StRoomEntity;
import mos.mosback.stRoom.domain.stRoom.StudyMemberEntity;
import mos.mosback.stRoom.domain.stRoom.StudyMemberRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRoomRepository extends JpaRepository<StudyMemberRoomEntity, Long> {

    List<StudyMemberRoomEntity> findAllByMember_MemberId(Long memberId);

    Optional<StudyMemberRoomEntity> findByMemberAndRoom(StudyMemberEntity studyMember, StRoomEntity stRoomEntity);
}