package mos.mosback.web.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mos.mosback.domain.posts.StRoomEntity;
import mos.mosback.domain.posts.StudyDaysEntity;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
public class StRoomSaveRequestDto {
    Long roomID;
    private String title;
    private String goal; //스터디 목표
    private String rules; //스터디 규칙
    private String quest; //생성 시 질문
    private String category; // 스터디 카테고리
    private String intro; //스터디 소개
    private int num; //멤버수
    private String mod; //스터디 분위기
    private boolean onOff; //진행방식 (온오프)
    private Date startDate; //스터디 시작 날짜
    private Date endDate; //스터디 끝나는 날짜
    private List<StudyDaysEntity> studyDayEntities;


    @Builder
    public StRoomSaveRequestDto(String title, String goal, String rules, String quest, String category,
                                String intro, int num, String mod, boolean onOff, Date startDate,
                                Date endDate,List<StudyDaysEntity> studyDayEntities) {
        this.title = title;
        this.goal = goal;
        this.rules = rules;
        this.quest = quest;
        this.category = category;
        this.intro = intro;
        this.mod = mod;
        this.num = num;
        this.onOff = onOff;
        this.startDate = startDate;
        this.endDate = endDate;
        this.studyDayEntities = studyDayEntities;

    }

    public StRoomEntity toEntity() {
        return StRoomEntity.builder()
                .title(title)
                .goal(goal)
                .rules(rules)
                .quest(quest)
                .category(category)
                .intro(intro)
                .mod(mod)
                .num(num)
                .onOff(onOff)
                .startDate(startDate)
                .endDate(endDate)
                .studyDayEntities(studyDayEntities)
                .build();
    }
}
