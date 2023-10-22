package mos.mosback.service;
import lombok.RequiredArgsConstructor;
import mos.mosback.domain.stRoom.MemberStatus;
import mos.mosback.domain.stRoom.StRoomEntity;
import mos.mosback.domain.stRoom.StudyMemberEntity;
import mos.mosback.login.domain.user.User;
import mos.mosback.login.domain.user.service.UserService;
import mos.mosback.login.global.jwt.service.JwtService;
import mos.mosback.repository.StRoomRepository;
import mos.mosback.repository.StudyMemberRepository;
import mos.mosback.stRoom.dto.StRoomSaveRequestDto;
import mos.mosback.stRoom.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class StRoomService {
    private final StRoomRepository stRoomRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final UserService userService;
    private final JwtService jwtService;

    public Long save(StRoomSaveRequestDto requestDto, HttpServletRequest req) {
        try {
            // 1. Study Room 저장
            StRoomEntity stRoom = stRoomRepository.save(requestDto.toEntity());

            // 2. 스터디 멤버 정보 저장할 변수 선언
            StudyMemberEntity studyMember = new StudyMemberEntity();

            // 3. Access Token 가져오기
            String accessToken = Optional.ofNullable(jwtService.extractAccessToken(req)).get().orElse("");

            // 4. Access Token으로 스터디 멤버 정보 가져오기 (User Entity를)
            String loginUserEmail = Optional.ofNullable(jwtService.extractEmail(accessToken)).get().orElse("");
            User user = userService.getUserByEmail(loginUserEmail);

            // 4. 정보 대입
            studyMember.setMemberId(user.getId());

            // 5. Study Member 저장
            studyMember.setStRoom(stRoom);
            studyMember.setStatus(MemberStatus.Leader);
            studyMemberRepository.save(studyMember);
            return stRoom.getRoomID();
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

        stRoomRepository.save(stroomEntity);
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
    public List<Home_RoomResponseDto> findPopularRoom() {
       return stRoomRepository.findPopularRoom();
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
            // 1. save할 변수 선언
            StudyMemberEntity studyMember = new StudyMemberEntity();

            // 2. 가입 시에는 룸ID를 요청 파라미터에서 받아서 StRoom 조회
            StRoomEntity stRoomEntity = stRoomRepository.findById(requestDto.getRoomID())
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id =" + requestDto.getRoomID()));

            // 3. 사용자 이메일 조회해서 save 전에 주입
            User user = userService.getUserByEmail(requestDto.getEmail());
            studyMember.setMemberId(user.getId());

            // 4. 조회된 정보 토대로 StudyMember save 처리
            studyMember.setStRoom(stRoomEntity);
            studyMember.setStatus(MemberStatus.Waiting);
            studyMember.setAnswer(requestDto.getAnswer());
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

    public String getMyInfo(String email) throws Exception {
        // 3. 사용자 이메일 조회해서 save 전에 주입
        User user = userService.getUserByEmail(email);

        List<StudyMemberEntity> memberJoinList = studyMemberRepository.findAllByMemberId(user.getId());

        // study member 가입이력이 있다면 "Y" , 없으면 "N"
        return !memberJoinList.isEmpty() ? "Y" : "N";
    }
    public String isRecruiting(Long roomID) {
        Optional<StRoomEntity> optionalRoom = stRoomRepository.findById(roomID);
        StRoomEntity stRoom = optionalRoom.get();
        if (stRoom.getStartDate().isAfter(LocalDate.now())) {
            stRoom.setRecruiting(true);
        } else {
            stRoom.setRecruiting(false);
        }
        return stRoom.isRecruiting() ? "true" : "false";
    }


}