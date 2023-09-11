package mos.mosback.controller;
import mos.mosback.web.dto.StRoomSaveRequestDto;
import mos.mosback.web.dto.StRoomListResponseDto;
import mos.mosback.web.dto.StRoomUpdateRequestDto;
import mos.mosback.service.StRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studyRoom") //URL 패턴
public class StRoomController {

    private final StRoomService stRoomService; // stRoomService를 주입.

    @Autowired
    public StRoomController(StRoomService stRoomService) {
        this.stRoomService = stRoomService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> saveroom(@RequestBody StRoomSaveRequestDto requestDto) {
        Long stroomId = stRoomService.save(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("created successfully. ID: " + stroomId);
    }

    /**
     * 스터디 방 정보 가져오기
     * @param roomID
     * @return
     */
    @GetMapping("/get/{roomID}")
    public ResponseEntity<StRoomUpdateRequestDto> getRoom(@PathVariable Long roomID) {
        StRoomUpdateRequestDto stroom = stRoomService.findById(roomID);
        return new ResponseEntity<>(stroom, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StRoomListResponseDto>> searchrooms(@RequestParam String keyword) {
        List<StRoomListResponseDto> strooms = stRoomService.findByTitleContaining(keyword);
        return new ResponseEntity<>(strooms, HttpStatus.OK);
    }

    @PutMapping("/update/{roomID}")
    public ResponseEntity<String> updateroom(@PathVariable Long roomID, @RequestBody StRoomUpdateRequestDto requestDto) {
        try {
            stRoomService.update(roomID, requestDto);
            return ResponseEntity.ok("updated.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("NOT FOUND"+"'"+roomID+"'");

        }
    }

    @DeleteMapping("/delete/{roomID}")
    public ResponseEntity<Void> deleteroom(@PathVariable Long roomID) {
        stRoomService.delete(roomID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<List<StRoomListResponseDto>> getAllrooms() {
        List<StRoomListResponseDto> strooms = stRoomService.findAllDesc();
        return new ResponseEntity<>(strooms, HttpStatus.OK);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<StRoomListResponseDto>> getPopularrooms() {
        List<StRoomListResponseDto> popularrooms = stRoomService.findPopularstrooms();
        return new ResponseEntity<>(popularrooms, HttpStatus.OK);
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
//getRecruitment : 모집기간인 게시물을 조회하는 엔드포인트.
