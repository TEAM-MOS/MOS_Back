package mos.mosback.service;

import lombok.RequiredArgsConstructor;
import mos.mosback.domain.stRoom.MemberStatus;
import mos.mosback.domain.stRoom.StRoomEntity;
import mos.mosback.domain.stRoom.StudyMemberEntity;
import mos.mosback.repository.StRoomRepository;
import mos.mosback.repository.StudyMemberRepository;
import mos.mosback.stRoom.dto.StRoomSaveRequestDto;
import mos.mosback.stRoom.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class StRoomService {
    private final StRoomRepository stRoomRepository;
    private final StudyMemberRepository studyMemberRepository;

    public Long save(StRoomSaveRequestDto requestDto) {
        try {
            StudyMemberEntity studyMember = new StudyMemberEntity();
            studyMember.setRoomId(requestDto.getRoomID());
            studyMember.setStatus(MemberStatus.Leader);
            studyMember.setMemberId(studyMember.getMemberId());
            studyMemberRepository.save(studyMember);
            return stRoomRepository.save(requestDto.toEntity()).getRoomID();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

}
    @Transactional
    public void update(Long roomID, StRoomUpdateRequestDto requestDto) {
        StRoomEntity stroomEntity = stRoomRepository.findById(roomID)
                .orElseThrow(() -> new IllegalArgumentException(roomID + " NOT FOUND"));

        // 나머지 필드 업데이트
        stroomEntity.update(
                requestDto.getTitle(), requestDto.getGoal(), requestDto.getRules(),
                requestDto.getQuest(), requestDto.getCategory(),
                requestDto.getIntro(), requestDto.getMaxMember(),
                requestDto.getMod(), requestDto.isOnOff(), requestDto.getLocation(),
                requestDto.getOnline(), requestDto.getStartDate(),
                requestDto.getEndDate(), requestDto.getStudyDayEntities());

    }  //stroomsRepository를 사용하여 데이터베이스에서 주어진 id에 해당하는 게시물을 찾기


    @Transactional(readOnly = true)
    public StRoomResponseDto findById(Long roomID) {
        StRoomEntity stRoomEntity = stRoomRepository.findById(roomID)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + roomID));

        return new StRoomResponseDto(stRoomEntity);
    }

    public List<Home_RoomResponseDto> findAllRoomsDesc() {
        return stRoomRepository.findAllDesc();
    }

    @Transactional
    public void delete(Long roomID) {
        StRoomEntity stroomEntity = stRoomRepository.findById(roomID)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id =" + roomID));

        stRoomRepository.delete(stroomEntity); //JpaRepository 에서 이미 delete 메소드를 지원하고 있으므로 활용
        //엔티티를 파라미터로 살제할 수도 있고, deleteById 메서드를 이용하면 id로 삭제할 수 있음
        // 존재하는 strooms인지 확인을 위해 엔티티 조회 후 그대로 삭제함
        // --> 서비스에서 delete 메서드를 만들면 컨트롤러가 사용하도록 컨트롤러에 코드 추가하기.
    }

    @Transactional(readOnly = true)
    public List<Home_RoomResponseDto> findByTitleContaining(String keyword) {
        return stRoomRepository.findByTitleContaining(keyword);
    }


    @Transactional(readOnly = true)
    public List<StRoomListResponseDto> findPopularRoom() {
        List<Home_RoomResponseDto> popularStrooms = stRoomRepository.findPopularRoom();
        return popularStrooms.stream()
                .map(StRoomListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Home_RoomResponseDto> findRoomsInHome() {

        return stRoomRepository.findHomeStRoomField();
    }

    public List<Home_RoomResponseDto> findByCategory(String category) {
        return stRoomRepository.findByCategory(category);
    }

    public void memberJoin(StRoomMemberJoinRequestDto requestDto) {
        try {
            StudyMemberEntity studyMember = new StudyMemberEntity();
            studyMember.setRoomId(requestDto.getRoomID());
            studyMember.setStatus(MemberStatus.Waiting);
            studyMember.setAnswer(requestDto.getAnswer());
          /* studyMember.setMemberId(studyMember.getMemberId());*/
            studyMemberRepository.save(studyMember);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Home_RoomResponseDto> getRecruitingStudies() {

        LocalDateTime now = LocalDateTime.now();
        List<Home_RoomResponseDto> recruitingStudies = stRoomRepository.findRecruitingStudies();

        return recruitingStudies;
    }

    public QuestionDto getQuestionById(Long roomId) {
         StRoomEntity stRoomEntity = stRoomRepository.findById(roomId)
                 .orElseThrow(() -> new EntityNotFoundException("Room not found"));

         QuestionDto responseDTO = new QuestionDto();
         responseDTO.setQuestion(stRoomEntity.getQuest());

         return responseDTO;

    }

}