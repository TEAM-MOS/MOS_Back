package mos.mosback.stRoom.dto;
import lombok.Getter;
import mos.mosback.stRoom.domain.stRoom.StRoomEntity;
import mos.mosback.stRoom.domain.stRoom.StudyDaysEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Getter
public class StRoomDetailResponseDto {
    private Long roomId;
    private String title;
    private String category;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean onOff; //진행방식 (온오프)
    private int memberNum; //현재 멤버수
    private int maxMember; //모집 멤버수
    private String mod; //스터디 분위기
    private List<StudyDaysEntity> studyDayEntities;
    private String goal;
    private String rules;
    private String intro;
    private LocalDateTime createdDate;
    private LocalDate deadline;

    public StRoomDetailResponseDto(StRoomEntity entity) {

        this.roomId = entity.getRoomId();
        this.createdDate = entity.getCreatedDate();
        this.title = entity.getTitle();
        this.location = entity.getLocation();
        this.intro = entity.getIntro();
        this.goal = entity.getGoal();
        this.rules = entity.getRules();
        this.category = entity.getCategory();
        this.memberNum = entity.getMemberNum();
        this.maxMember = entity.getMaxMember();
        this.mod = entity.getMod();
        this.onOff = entity.isOnOff();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.deadline = entity.getStartDate().minusDays(1);
        this.studyDayEntities = entity.getStudyDayEntities();



    } //스터디 + 투두 리스트의 스터디룸 상세화면
}

