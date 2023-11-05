package mos.mosback.stRoom.controller;
import lombok.RequiredArgsConstructor;
import mos.mosback.login.domain.user.dto.UserProfileDto;
import mos.mosback.login.domain.user.repository.UserRepository;
import mos.mosback.login.domain.user.service.UserService;
import mos.mosback.stRoom.dto.*;
import mos.mosback.stRoom.service.StRoomService;
import mos.mosback.stRoom.service.ToDoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
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
    private final UserService userService;
    private final UserRepository userRepository;
    @PostMapping("/create")
    public ResponseEntity<String> saveRoom(@RequestBody StRoomSaveRequestDto requestDto, HttpServletRequest req) {
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        requestDto.setEmail(currentEmail);
        Long stroomId = stRoomService.save(requestDto, req);
        if (stroomId != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body
                    ("message: created successfully. ID:" + stroomId +"\nsuccess:true \nstatus:201");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                    ("message: Bad Request \nsuccess:false \nstatus:400");
        }
    }
    @GetMapping("/my{roomId}")
    public ResponseEntity<StRoomResponseDto> FindByID (@PathVariable Long roomId) {

            StRoomResponseDto stroom = stRoomService.findById(roomId);
            return new ResponseEntity<>(stroom, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchRoom(@RequestParam String keyword) {
        try {
            List<Home_RoomResponseDto> strooms = stRoomService.findByTitleContaining(keyword);
            return new ResponseEntity<>(strooms, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "404");
            response.put("message", "NOT FOUND " + keyword);
            response.put("success", "false");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    @PutMapping("/update/{roomId}")
    public ResponseEntity<String> UpdateRoom(@PathVariable Long roomId, @RequestBody StRoomUpdateRequestDto requestDto) {
        try {
            stRoomService.update(roomId, requestDto);
            return ResponseEntity.ok
                    ("message: updated successfully. roomId:"+roomId+"\nsuccess:true\nstatus:200");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("message: NOT FOUND "+"'"+roomId+"'"+"\nsuccess:false\nstatus:404");
        }
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Map<String, Object>> deleteRoom(@PathVariable Long roomId) {
        Map<String, Object> response = new HashMap<>();
        try {
            stRoomService.delete(roomId);
            response.put("status", 200);
            response.put("success", true);
            response.put("message", "deleted"+"'"+roomId+"'");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException ex) {
            response.put("status", 404);
            response.put("success", false);
            response.put("message", "NOTFOUND"+"'"+roomId+"'");
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
            response.put("message", "NOT FOUND");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/myPage/{memberId}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable Long memberId) {
        Map<String, Object> response = new HashMap<>();
        try {
            UserProfileDto userProfileDto = stRoomService.getMemberProfileById(memberId);
            response.put("status", 200);
            response.put("success", true);
            response.put("data", userProfileDto);

            return ResponseEntity.ok(response);
        } catch (Exception e) {

            response.put("status", 500);
            response.put("success", false);
            response.put("message", "오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
               response.put("message", "NOT FOUND");
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
           }

        }
    }
    @PostMapping("/memberjoin/{roomId}")
    public ResponseEntity<String> memberJoin(@PathVariable Long roomId,
                                             @RequestBody StRoomMemberJoinRequestDto requestDto) {
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        requestDto.setEmail(currentEmail);
        requestDto.setRoomId(roomId);
        stRoomService.memberJoin(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body
                ("message : joined successfully.\nstatus : 201\n success: true");
    }
    
    @PostMapping("/accept/{roomId}")
    public ResponseEntity<String> acceptMember(@PathVariable Long roomId,
                                             @RequestBody AcceptMemberRequestDto requestDto) {
        try {
            requestDto.setRoomId(roomId);
            stRoomService.acceptMember(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body
                    ("message : Accepted successfully.\nstatus : 201\n success: true");
        }catch (IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("message : ServerError.ServerError.\nstatus : 500\n success: false");
        }
    }
    @PostMapping("/reject/{roomId}")
    public ResponseEntity<String> rejectMember(@PathVariable Long roomId,
                                             @RequestBody AcceptMemberRequestDto requestDto) {
        try {
            requestDto.setRoomId(roomId);
            stRoomService.rejectMember(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body
                    ("message : Rejected successfully.\nstatus : 201\n success: true");
        }catch (IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("message : ServerError.ServerError.\nstatus : 500\n success: false");
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
            response.put("message", "NOT FOUND");

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

    @GetMapping("/Info/{roomId}")
    public ResponseEntity<Map<String, Object>> recruitInfo(@PathVariable Long roomId) {
        String recruitInfo = stRoomService.isRecruiting(roomId);
       StRoomDetailResponseDto stroom = stRoomService.findByRoomId(roomId);
        List<StRoomToDoResponseDto> todoList = toDoService.findStRoomTodoByRoomId(roomId);
        List<StRoomMemberResponseDto> studyRoomMemberList = stRoomService.getStudyRoomMemberList(roomId);

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
