package mos.mosback.stRoom.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mos.mosback.stRoom.domain.stRoom.StudyDaysEntity;

import java.time.LocalDate;
import java.util.List;


@Getter
@NoArgsConstructor
public class StRoomUpdateRequestDto {
    
    Long roomId;
    private String title;
    private String goal; //스터디 목표
    private String rules; //스터디 규칙
    private String quest; //생성 시 질문
    private String category; // 스터디 카테고리
    private String intro; //스터디 소개
    private int maxMember; // 모집 멤버수
    private String mod; //스터디 분위기
    private boolean onOff; //진행방식 (온오프)
    private String location; //스터디 장소
    private int online; // 온라인일 경우
    // 1 : 줌 2 : 디코 3: 구글미트 4: 기타
    private LocalDate startDate; //스터디 시작 날짜
    private LocalDate endDate; //스터디 끝나는 날짜
    private List<StudyDaysEntity> studyDayEntities;


    @Builder
    public StRoomUpdateRequestDto(String title, String goal, String rules, String quest, String category,
                                  String intro, int maxMember ,String mod, boolean onOff,String location,int online,
                                  LocalDate startDate, LocalDate endDate, List<StudyDaysEntity> studyDayEntities) {
        this.title = title;
        this.goal = goal;
        this.rules = rules;
        this.quest = quest;
        this.category = category;
        this.intro = intro;
        this.mod = mod;
        this.maxMember = maxMember;
        this.onOff = onOff;
        this.location = location;
        this.online = online;
        this.startDate = startDate;
        this.endDate = endDate;
        this.studyDayEntities = studyDayEntities;

    }


}