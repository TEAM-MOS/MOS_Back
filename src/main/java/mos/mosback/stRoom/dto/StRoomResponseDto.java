package mos.mosback.stRoom.dto;
import lombok.Getter;
import lombok.Setter;
import mos.mosback.stRoom.domain.stRoom.StRoomEntity;
import mos.mosback.stRoom.domain.stRoom.StudyDaysEntity;

import java.time.LocalDate;
import java.util.List;


@Setter
@Getter
public class StRoomResponseDto {
    Long roomId;
    private String title;
    private String goal; //스터디 목표
    private String rules; //스터디 규칙
    private String category; // 스터디 카테고리
    private int memberNum; //현재 멤버수
    private String mod; //스터디 분위기
    private boolean onOff; //진행방식 (온오프)
    private LocalDate startDate; //스터디 시작 날짜
    private LocalDate endDate; //스터디 끝나는 날짜
    private List<StudyDaysEntity> studyDayEntities;
    private String nickname;
    /*유저프로필 + 사진*/


    public StRoomResponseDto(StRoomEntity entity) {

        this.roomId = entity.getRoomId();
        this.title = entity.getTitle();
        this.goal = entity.getGoal();
        this.rules = entity.getRules();
        this.category =entity.getCategory();
        this.memberNum = entity.getMemberNum();
        this.mod = entity.getMod();
        this.onOff = entity.isOnOff();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.studyDayEntities =entity.getStudyDayEntities();


    } //스터디 + 투두 리스트의 스터디룸 상세화면


}
//Entity의 필드 이루만 사용하므로 생성자로 Entity를 받아 필드에 값을 넣어줌
//상세정보에 노출 될 필드