package mos.mosback.controller;
import mos.mosback.domain.posts.StRoomEntity;
import mos.mosback.web.dto.*;
import mos.mosback.service.StRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/studyRoom") //URL 패턴
public class StRoomController {

    private final StRoomService stRoomService; // stRoomService를 주입.

    @Autowired
    public StRoomController(StRoomService stRoomService) {
        this.stRoomService = stRoomService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> saveRoom(@RequestBody StRoomSaveRequestDto requestDto) {
        Long stroomId = stRoomService.save(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("created successfully. ID: " + stroomId);
    }

    @GetMapping("/MyStudy/{roomId}")
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
            return ResponseEntity.ok("updated.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("NOT FOUND "+"'"+roomId+"'");
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
    public ResponseEntity<List<Home_RoomResponseDto>> findAllRoomsDesc() {
        List<Home_RoomResponseDto> rooms = stRoomService.findAllRoomsDesc();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<StRoomListResponseDto>> getPopularRooms() {
        List<StRoomListResponseDto> popularrooms = stRoomService.findPopularRoom();
        return new ResponseEntity<>(popularrooms, HttpStatus.OK);
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
            response.put("message", "NOT FOUND: Quest with roomID " + roomId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    @PostMapping("/memberjoin")
    public ResponseEntity<String> memberJoin(@RequestBody StRoomMemberJoinRequestDto requestDto) {
        stRoomService.memberJoin(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("joined successfully.");
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
            response.put("message", "NOT FOUND: recruiting studyRoom ");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


}


//핸들러 메서드 :

//create: 새 게시물을 생성
///MyStudy/{roomId}: 특정 roomID에 해당하는 게시물을 조회( 스터디그룹 -마이스터디화면 조회 )
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
//