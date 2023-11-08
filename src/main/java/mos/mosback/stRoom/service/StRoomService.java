package mos.mosback.stRoom.service;
import lombok.RequiredArgsConstructor;
import mos.mosback.login.domain.user.dto.UserProfileDto;
import mos.mosback.login.domain.user.repository.UserRepository;
import mos.mosback.stRoom.domain.stRoom.MemberStatus;
import mos.mosback.stRoom.domain.stRoom.StRoomEntity;
import mos.mosback.stRoom.domain.stRoom.StudyMemberEntity;
import mos.mosback.login.domain.user.User;
import mos.mosback.login.domain.user.service.UserService;
import mos.mosback.login.global.jwt.service.JwtService;
import mos.mosback.stRoom.repository.StRoomRepository;
import mos.mosback.stRoom.repository.StudyMemberRepository;
import mos.mosback.stRoom.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class StRoomService {
    private final StRoomRepository stRoomRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public Long save(StRoomSaveRequestDto requestDto, HttpServletRequest req) {
        try {
            // 1. Study Room 저장
            StRoomEntity stRoom = stRoomRepository.save(requestDto.toEntity());

            // 2. 스터디 멤버 정보 저장할 변수 선언
            StudyMemberEntity studyMember = new StudyMemberEntity();

            // 3. Access Token 가져오기
            String accessToken = Optional.ofNullable(jwtService.extractAccessToken(req)).get().orElse("");

            // 4. Access Token으로 스터디 멤버 정보 가져오기 (User Entity를)
            String loginUserEmail = Optional.ofNullable(jwtService.extractEmailFromJwt(accessToken)).get().orElse("");
            User user = userService.getUserByEmail(loginUserEmail);


            // 5. Study Member 저장
            studyMember.setMemberId(user.getId());
            studyMember.setStRoom(stRoom);
            studyMember.setStatus(MemberStatus.Leader);
            studyMemberRepository.save(studyMember);

            return stRoom.getRoomId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Transactional
    public void update(Long roomId, StRoomUpdateRequestDto requestDto) {
        StRoomEntity stroomEntity = stRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException(roomId + " NOT FOUND"));

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
    public StRoomResponseDto findById(Long roomId)  {
      try {

          StRoomEntity stRoomEntity = stRoomRepository.findById(roomId)
                  .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + roomId));

          return new StRoomResponseDto(stRoomEntity);
      }catch (Exception e){
          e.printStackTrace();
          return null;
      }
    }

    @Transactional(readOnly = true)
    public StRoomDetailResponseDto findByRoomId(Long roomId) {
        StRoomEntity stRoomEntity = stRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + roomId));

        return new StRoomDetailResponseDto(stRoomEntity);
    }
    public List<Home_RoomResponseDto> findAllRoomsDesc() {


        return stRoomRepository.findAllDesc();
    }

    @Transactional
    public void delete(Long roomId) {
        StRoomEntity stroomEntity = stRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id =" + roomId));

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
            StRoomEntity stRoomEntity = stRoomRepository.findById(requestDto.getRoomId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id =" + requestDto.getRoomId()));

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

    public QuestionAndAnswerResponseDto getQuestionAndAnswerById(Long memberId, Long roomId) {

        StudyMemberEntity memberEntity = studyMemberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        StRoomEntity stRoomEntity = stRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));

        QuestionAndAnswerResponseDto responseDto = new QuestionAndAnswerResponseDto();
        responseDto.setMemberId(memberEntity.getMemberId());
        responseDto.setAnswer(memberEntity.getAnswer());
        responseDto.setQuestion(stRoomEntity.getQuest());


        return responseDto;
    }
    public UserProfileDto getMemberProfileById(Long memberId) throws Exception {

        Optional<User> optionalUser = userRepository.findById(memberId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // 엔터티 정보를 DTO로 매핑하여 반환
            return new UserProfileDto(
                    user.getId(),
                    user.getNickname(),
                    user.getName(),
                    user.getStr_duration(),
                    user.getEnd_duration(),
                    user.getMessage(),
                    user.getCompany(),
                    user.getTend1(),
                    user.getTend2(),
                    user.getRoomId(),
                    user.getImageUrl()
            );
        } else {
            throw new Exception("해당 이메일의 사용자를 찾을 수 없습니다: " + memberId);
        }
    }

    public String getMyInfo(String email) throws Exception {
        // 3. 사용자 이메일 조회해서 save 전에 주입
        User user = userService.getUserByEmail(email);

        List<StudyMemberEntity> memberJoinList = studyMemberRepository.findAllByMemberId(user.getId());

        // study member 가입이력이 있다면 "Y" , 없으면 "N"
        return !memberJoinList.isEmpty() ? "Y" : "N";
    }

    public String isRecruiting(Long roomId) {
        Optional<StRoomEntity> optionalRoom = stRoomRepository.findById(roomId);
        StRoomEntity stRoom = optionalRoom.get();
        if (stRoom.getStartDate().isAfter(LocalDate.now())) {
            stRoom.setRecruiting(true);
        } else {
            stRoom.setRecruiting(false);
        }
        return stRoom.isRecruiting() ? "true" : "false";
    }

    public void acceptMember(AcceptMemberRequestDto requestDto){
        // 1. save할 변수 선언
        StudyMemberEntity studyMember = new StudyMemberEntity();

        // 2. 가입 시에는 룸ID를 요청 파라미터에서 받아서 StRoom 조회
        StRoomEntity stRoomEntity = stRoomRepository.findById(requestDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id =" + requestDto.getRoomId()));

        studyMember.setMemberId(requestDto.getMemberId());
        studyMember.setStRoom(stRoomEntity);
        studyMember.setStatus(MemberStatus.Member);
        studyMemberRepository.save(studyMember);

    }
    public void rejectMember(AcceptMemberRequestDto requestDto){
        // 1. save할 변수 선언
        StudyMemberEntity studyMember = new StudyMemberEntity();

        // 2. 가입 시에는 룸ID를 요청 파라미터에서 받아서 StRoom 조회
        StRoomEntity stRoomEntity = stRoomRepository.findById(requestDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id =" + requestDto.getRoomId()));

        studyMember.setMemberId(requestDto.getMemberId());
        studyMember.setStRoom(stRoomEntity);
        studyMember.setStatus(MemberStatus.Rejected);
        studyMemberRepository.save(studyMember);

    }

    public List<StRoomMemberResponseDto> getStudyRoomMemberList(Long roomId) {
        StRoomEntity stRoom = stRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        List<StudyMemberEntity> memberEntityList = studyMemberRepository.findAllByStRoom(stRoom);
        List<StRoomMemberResponseDto> memberList = new ArrayList<>();
        for (StudyMemberEntity item : memberEntityList) {
            StRoomMemberResponseDto dto = new StRoomMemberResponseDto();
            dto.setMemberId(item.getMemberId());
            dto.setStatus(item.getStatus());
            memberList.add(dto);
        }
        return memberList;
    }

    public User getUserInfo(Long memberId) {
        return userRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("user info not found"));
    }

    public List<StudyMemberHistoryDto> getMyStudyMemberHistory(String email) throws Exception {
        // 3. 사용자 이메일 조회해서 save 전에 주입
        User user = userService.getUserByEmail(email);
        List<StudyMemberEntity> memberJoinList = studyMemberRepository.findAllByMemberId(user.getId());
        memberJoinList = memberJoinList.stream()
                .filter(data -> !data.getStatus().equals(MemberStatus.Leader))
                .collect(Collectors.toList());
        List<StudyMemberHistoryDto> result = new ArrayList<>();
        for (StudyMemberEntity item : memberJoinList) {
            StudyMemberHistoryDto data = new StudyMemberHistoryDto();
            data.setTitle(item.getStRoom().getTitle());
            data.setStatus(item.getStatus());
            result.add(data);
        }
        return result;
    }


    public List<StRoomResponseDto> getLeaderStudies(String userEmail) {
        // 사용자의 이메일을 기반으로 Leader로 등록된 스터디룸을 조회하여 StRoomEntity 목록을 얻어옴
        List<StRoomEntity> leaderStudies = stRoomRepository.findByCreatedByUserEmail(userEmail);

        // StRoomEntity 목록을 StRoomResponseDto 목록으로 변환하여 반환
        return leaderStudies.stream()
                .map(StRoomResponseDto::new)
                .collect(Collectors.toList());
    }

}