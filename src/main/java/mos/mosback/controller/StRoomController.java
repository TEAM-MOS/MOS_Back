package mos.mosback.controller;
import mos.mosback.web.dto.*;
import mos.mosback.service.StRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
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

    /**
     * 스터디 방 정보 가져오기
     * @param //roomId
     * @return
     */

    @GetMapping("get/{roomId}")
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

    @DeleteMapping("/delete/{roomId}")
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

    @PostMapping("/memberjoin")
    public ResponseEntity<String> memberJoin(@RequestBody StRoomMemberJoinRequestDto requestDto) {
        Long stroomId = stRoomService.memberJoin(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("created successfully. ID: " + stroomId);
    }


}
//핸들러 메서드 :
//
//create: 새 게시물을 생성하는 엔드포인트.
//getstroom: 특정 roomID에 해당하는 게시물을 조회하는 엔드포인트.( 스터디그룹 조회 )
//searchstrooms: 키워드가 포함된 게시물을 검색하는 엔드포인트.
//updatestroom: 게시물을 업데이트하는 엔드포인트.
//deletestroom: 게시물을 삭제하는 엔드포인트.
//getAllstrooms: 모든 게시물을 조회하는 엔드포인트.
//getPopularstrooms: 조회순으로 게시물을 조회하는 엔드포인트. (인기순 조회시)
//memberjoin - 스터디 가입 API
//https://blog.pumpkin-raccoon.com/115#:~:text=1%201.%20%EB%B3%B5%EC%88%98%20%3E%20%EB%8B%A8%EC%88%98%20REST%20API%EC%97%90%EC%84%9C%EB%8A%94%20post%2C,%EC%BB%AC%EB%A0%89%EC%85%98%20%ED%95%84%ED%84%B0%EB%A7%81%3A%20URL%20%EC%BF%BC%EB%A6%AC%20%3E%20%EC%83%88%EB%A1%9C%EC%9A%B4%20API%20