package mos.mosback.service;
import lombok.RequiredArgsConstructor;
import mos.mosback.domain.posts.StRoomEntity;
import mos.mosback.repository.StRoomRepository;
import mos.mosback.web.dto.StRoomSaveRequestDto;
import mos.mosback.web.dto.StRoomListResponseDto;
import mos.mosback.web.dto.StRoomUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class StRoomService {
    private final StRoomRepository stRoomRepository;

    @Transactional
    public  Long save(StRoomSaveRequestDto requestDto){
        return stRoomRepository.save(requestDto.toEntity()).getRoomID();
    }
    //스터디를 생성하고 아이디를 반환.

    @Transactional
    public void update(Long roomID, StRoomUpdateRequestDto requestDto) {
        StRoomEntity stroomEntity = stRoomRepository.findById(roomID)
                .orElseThrow(() -> new IllegalArgumentException(roomID + " NOT FOUND"));

        // 나머지 필드 업데이트
        stroomEntity.update(requestDto.getTitle(), requestDto.getGoal(), requestDto.getRules(),
                requestDto.getQuest(),requestDto.getCategory(),
                requestDto.getIntro(), requestDto.getNum(), requestDto.getMod(),
                requestDto.isOnOff(), requestDto.getStartDate(), requestDto.getEndDate()
                ,requestDto.getStudyDayEntities());

    }  //stroomsRepository를 사용하여 데이터베이스에서 주어진 id에 해당하는 게시물을 찾기


    @Transactional(readOnly = true)
    public StRoomUpdateRequestDto findById(Long roomID) {
        StRoomEntity entity = stRoomRepository.findById(roomID)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + roomID));

        return new StRoomUpdateRequestDto(entity);
    }

    @Transactional(readOnly = true)
    public List<StRoomListResponseDto> findAllDesc() {
        return stRoomRepository.findAllDesc().stream()
                .map(StRoomListResponseDto::new) //.map(stroom->new stroomResponse(strooms))랑 같음
                // stroomsRepository 결과로 넘어온 strooms의 Stream을
                // map을 통해 stroomsListResponseDto변환 ->List로 반환하는 메소드

                .collect(Collectors.toList());
    }

    @Transactional
    public  void delete (Long roomID){
        StRoomEntity stroomEntity = stRoomRepository.findById(roomID)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id =" + roomID));

        stRoomRepository.delete(stroomEntity); //JpaRepository 에서 이미 delete 메소드를 지원하고 있으므로 활용
        //엔티티를 파라미터로 살제할 수도 있고, deleteById 메서드를 이용하면 id로 삭제할 수 있음
        // 존재하는 strooms인지 확인을 위해 엔티티 조회 후 그대로 삭제함
        // --> 서비스에서 delete 메서드를 만들면 컨트롤러가 사용하도록 컨트롤러에 코드 추가하기.
    }
    @Transactional(readOnly = true)
    public List<StRoomListResponseDto> findByTitleContaining(String keyword) {
        List<StRoomEntity> strooms = stRoomRepository.findByTitleContaining(keyword);
        if (strooms.isEmpty()) {
            throw new IllegalArgumentException("해당 스터디가 없습니다.");
        }
        return strooms.stream()
                .map(StRoomListResponseDto::new)
                .collect(Collectors.toList());
    } //스터디 title 로 검색


    @Transactional(readOnly = true)
    public List<StRoomListResponseDto> findPopularstrooms() {
        // 클릭된 조회수 순으로 게시물을 조회하는 비즈니스 로직을 호출
        List<StRoomEntity> popularStrooms = stRoomRepository.findPopularstrooms();
        return popularStrooms.stream()
                .map(StRoomListResponseDto::new)
                .collect(Collectors.toList());
    }


}
