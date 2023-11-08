package mos.mosback.stRoom.controller;
import lombok.RequiredArgsConstructor;
import mos.mosback.login.domain.user.User;
import mos.mosback.login.domain.user.dto.UserProfileDto;
import mos.mosback.stRoom.domain.stRoom.MemberStatus;
import mos.mosback.stRoom.domain.stRoom.StRoomEntity;
import mos.mosback.stRoom.dto.*;
import mos.mosback.stRoom.service.StRoomService;
import mos.mosback.stRoom.service.ToDoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import software.amazon.ion.NullValueException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/studyRoom") //URL 패턴
public class StRoomController {

    private final StRoomService stRoomService; // stRoomService를 주입.
    private final ToDoService toDoService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> saveRoom(@RequestBody StRoomSaveRequestDto requestDto, HttpServletRequest req) {
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        requestDto.setEmail(currentEmail);
        Long stroomId = stRoomService.save(requestDto, req);
        Map<String, Object> response = new HashMap<>();
        if (stroomId != null) {

            response.put("status", 201);
            response.put("message", "스터디생성완료");
            response.put("roomId",stroomId);
            response.put("success", true);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } else {
            response.put("status", 500);
            response.put("message", "서버내부오류");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping("/my{roomId}")
    public ResponseEntity<?> FindByID (@PathVariable Long roomId) {

        StRoomResponseDto stroom = stRoomService.findById(roomId);

        try {
            List<StRoomMemberResponseDto> studyRoomMemberList = stRoomService.getStudyRoomMemberList(roomId);
            StRoomMemberResponseDto leaderInfo = studyRoomMemberList.stream()
                    .filter(data -> MemberStatus.Leader.name().equals(data.getStatus().name()))
                    .findFirst().orElseThrow(() -> new EntityNotFoundException("leader info not found"));
            User leaderUser = stRoomService.getUserInfo(leaderInfo.getMemberId());
            stroom.setNickname(leaderUser.getNickname());
            stroom.setMemberNum(studyRoomMemberList.size());

            return new ResponseEntity<>(stroom, HttpStatus.OK);
        }catch (Exception e){
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("message", "해당 스터디룸 없음" );
            response.put("success", false);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchRoom(@RequestParam String keyword) {
        try {
            List<Home_RoomResponseDto> strooms = stRoomService.findByTitleContaining(keyword);
            return new ResponseEntity<>(strooms, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("message", "존재하지 않는 키워드: " + keyword);
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    @PutMapping("/update/{roomId}")
    public ResponseEntity<Map<String, Object>> UpdateRoom(@PathVariable Long roomId, @RequestBody StRoomUpdateRequestDto requestDto) {
        try {
            Map<String, Object> response = new HashMap<>();
            stRoomService.update(roomId, requestDto);
            response.put("status", 201);
            response.put("message", "수정완료");
            response.put("success", true);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("message", "해당 스터디룸 없음");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Map<String, Object>> deleteRoom(@PathVariable Long roomId) {
        Map<String, Object> response = new HashMap<>();
        try {
            stRoomService.delete(roomId);
            response.put("status", 200);
            response.put("success", true);
            response.put("message", "삭제완료 : "+"'"+roomId+"'");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException ex) {
            response.put("status", 404);
            response.put("success", false);
            response.put("message", "해당 스터디룸 없음");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Home_RoomResponseDto>> findAllRoomsDesc(){
        List<Home_RoomResponseDto> rooms = stRoomService.findAllRoomsDesc();
           return ResponseEntity.ok(rooms);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Home_RoomResponseDto>> getPopularRooms() {
        List<Home_RoomResponseDto> rooms = stRoomService.findPopularRoom();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/home/studyRoom")
    public ResponseEntity<List<Home_RoomResponseDto>> findRoomsInHome(){
        List<Home_RoomResponseDto> roomsInhome = stRoomService.findRoomsInHome();

        return new ResponseEntity<>(roomsInhome,HttpStatus.OK);
    }

    @GetMapping("/byCategory/{category}")
    public ResponseEntity<Map<String, Object>> getStudyRoomsByCategory(@PathVariable String category) {
        Map<String, Object> response = new HashMap<>();
        List<Home_RoomResponseDto> studyRooms = stRoomService.findByCategory(category);

        if (studyRooms.isEmpty()) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("success", false);
            response.put("message", "NOT FOUND: " + category);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("status", HttpStatus.OK.value());
        response.put("success", true);
        response.put("studyRooms", studyRooms); // 데이터를 직접 넣음
        return ResponseEntity.ok(response);
    }


    @GetMapping("/question/{roomId}")
    public ResponseEntity<?> getQuestionById(@PathVariable Long roomId) {
        try {
            QuestionDto question = stRoomService.getQuestionById(roomId);
            return ResponseEntity.ok(question);
        } catch (EntityNotFoundException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("status:", HttpStatus.NOT_FOUND.value());
            response.put("success", false);
            response.put("message", "해당 스터디룸 없음");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/myPage/{memberId}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable Long memberId) {
        Map<String, Object> response = new HashMap<>();
        try {
            UserProfileDto userProfileDto = stRoomService.getMemberProfileById(memberId);
            if(userProfileDto != null) {
                response.put("status", 200);
                response.put("success", true);
                response.put("data", userProfileDto);
                return ResponseEntity.ok(response);
            }
            else{
                response.put("status", 404);
                response.put("success", false);
                response.put("message", "해당멤버를 찾을 수 없음");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {

            response.put("status", 500);
            response.put("success", false);
            response.put("message", "서버내부오류");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @GetMapping("/QnA/{memberId}/{roomId}")
    public ResponseEntity<?> getQuestionandAnswerById(@PathVariable("memberId") Long memberId, @PathVariable("roomId") Long roomId) {
        {
           try {
               QuestionAndAnswerResponseDto QnA = stRoomService.getQuestionAndAnswerById(memberId,roomId);
               return ResponseEntity.ok(QnA);
           }catch (EntityNotFoundException ex) {
               Map<String, Object> response = new HashMap<>();
               response.put("status:", HttpStatus.NOT_FOUND.value());
               response.put("success", false);
               response.put("message", "요청을 찾을 수 없음");
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
           }

        }
    }
    @PostMapping("/memberjoin/{roomId}")
    public ResponseEntity<Map<String, Object>> memberJoin(@PathVariable Long roomId,
                                             @RequestBody StRoomMemberJoinRequestDto requestDto) {
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        requestDto.setEmail(currentEmail);
        requestDto.setRoomId(roomId);
        stRoomService.memberJoin(requestDto);
        try{
            Map<String, Object> response = new HashMap<>();
            response.put("status:", HttpStatus.OK.value());
            response.put("success", true);
            response.put("message", "가입완료");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (IllegalArgumentException e){
            Map<String, Object> response = new HashMap<>();
            response.put("status:", HttpStatus.NOT_FOUND.value());
            response.put("success", false);
            response.put("message", "해당스터디룸 없음");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/accept/{roomId}")
    public ResponseEntity<Map<String, Object>> acceptMember(@PathVariable Long roomId,
                                             @RequestBody AcceptMemberRequestDto requestDto) {
        try {
            requestDto.setRoomId(roomId);
            stRoomService.acceptMember(requestDto);
            Map<String, Object> response = new HashMap<>();
            response.put("status:", HttpStatus.OK.value());
            response.put("success", true);
            response.put("message", "승인완료");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (IllegalArgumentException ex){
            Map<String, Object> response = new HashMap<>();
            response.put("status:", HttpStatus.BAD_REQUEST.value());
            response.put("success", false);
            response.put("message", "잘못된 요청");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @PostMapping("/reject/{roomId}")
    public ResponseEntity<Map<String, Object>> rejectMember(@PathVariable Long roomId,
                                             @RequestBody AcceptMemberRequestDto requestDto) {
        try {
            requestDto.setRoomId(roomId);
            stRoomService.rejectMember(requestDto);
            Map<String, Object> response = new HashMap<>();
            response.put("status:", HttpStatus.OK.value());
            response.put("success", true);
            response.put("message", "승인거절완료");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (IllegalArgumentException ex){
            Map<String, Object> response = new HashMap<>();
            response.put("status:", HttpStatus.BAD_REQUEST.value());
            response.put("success", false);
            response.put("message", "잘못된 요청");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }



    @GetMapping("/recruiting")
    public ResponseEntity<?> getRecruitingStudies() {
        List<Home_RoomResponseDto> recruitingStudies = stRoomService.getRecruitingStudies();

        if (!recruitingStudies.isEmpty()) {
            return ResponseEntity.ok(recruitingStudies);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("success", false);
            response.put("message", "모집중인 스터디룸을 찾을 수 없음");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * 스터디 가입여부를 조회하는 API
     * @return
     */
    @GetMapping("/my-info")
    public ResponseEntity<Map<String, Object>> getMyInfo() throws Exception {
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // 현재 사용자의 이메일
        String joinYn = stRoomService.getMyInfo(email);
        Map<String, Object> response = new HashMap<>();
        response.put("joinYn", joinYn);
        response.put("success", true);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 스터디 가입여부를 조회하는 API
     * @return
     */
    @GetMapping("/my-study-member-history")
    public ResponseEntity<Map<String, Object>> getMyStudyMemberHistory() throws Exception {
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // 현재 사용자의 이메일
        List<StudyMemberHistoryDto> memberHistoryList = stRoomService.getMyStudyMemberHistory(email);
        Map<String, Object> response = new HashMap<>();
        response.put("memberHistoryList", memberHistoryList);
        response.put("success", true);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/Info/{roomId}")
    public ResponseEntity<Map<String, Object>> recruitInfo(@PathVariable Long roomId) {
        String recruitInfo = stRoomService.isRecruiting(roomId);
       StRoomDetailResponseDto stroom = stRoomService.findByRoomId(roomId);
        List<StRoomToDoResponseDto> todoList = toDoService.findStRoomTodoByRoomId(roomId);
        List<StRoomMemberResponseDto> studyRoomMemberList = stRoomService.getStudyRoomMemberList(roomId);
        StRoomMemberResponseDto leaderInfo = studyRoomMemberList.stream()
                .filter(data -> MemberStatus.Leader.name().equals(data.getStatus().name()))
                .findFirst().orElseThrow(() -> new EntityNotFoundException("leader info not found"));
        User leaderUser = stRoomService.getUserInfo(leaderInfo.getMemberId());
        stroom.setNickname(leaderUser.getNickname());
        stroom.setMemberNum(studyRoomMemberList.size());

        Map<String, Object> response = new HashMap<>();
        response.put("모집", recruitInfo);
        response.put("StudyRoom", stroom);
        response.put("todoList", todoList);
        response.put("studyRoomMemberList", studyRoomMemberList);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }


}


//핸들러 메서드 :

//create: 새 게시물을 생성
///MyStudy/{roomId}: 특정 roomId에 해당하는 게시물을 조회( 스터디그룹 -마이스터디화면 조회 )
//search: 제목에 키워드가 포함된 게시물을 검색하는 엔드포인트.
///update/{roomId}: 게시물 수정
///(Delete) {roomId}: 게시물을 삭제
//getPopularstrooms: 조회순으로 게시물을 조회(인기순 조회시)
//all: 전체 스터디 조회
///question/{roomId}: 스터디장이 남긴 질문 조회
//memberjoin - 스터디 가입 API
///byCategory/{category} : 카테고리별로 조회하는 엔프포인트
//https://blog.pumpkin-raccoon.com/115#:~:text=1%201.%20%EB%B3%B5%EC%88%98%20%3E%20%EB%8B%A8%EC%88%98%20REST%20API%EC%97%90%EC%84%9C%EB%8A%94%20post%2C,%EC%BB%AC%EB%A0%89%EC%85%98%20%ED%95%84%ED%84%B0%EB%A7%81%3A%20URL%20%EC%BF%BC%EB%A6%AC%20%3E%20%EC%83%88%EB%A1%9C%EC%9A%B4%20API%20
///recruiting : 모집중인 게시물을 조회하는 엔드포인트.
///my-info : 스터디 가입여부 조회