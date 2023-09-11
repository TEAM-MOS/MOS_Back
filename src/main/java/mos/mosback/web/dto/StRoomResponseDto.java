package mos.mosback.web.dto;
import lombok.Getter;
import mos.mosback.domain.posts.StRoomEntity;
import mos.mosback.domain.posts.StudyDaysEntity;

import java.util.Date;
import java.util.List;


@Getter
public class StRoomResponseDto {

    Long roomID;
    private String title;
    private String goal; //스터디 목표
    private String rules; //스터디 규칙
    private String quest;
    private String category; // 스터디 카테고리
    private String intro; //스터디 소개
    private int num; //멤버수
    private String mod; //스터디 분위기
    private int click;// 클릭횟수 (인기순 조회)
    private boolean onOff; //진행방식 (온오프)
    private Date startDate; //스터디 시작 날짜
    private Date endDate; //스터디 끝나는 날짜
    private Date createDate; // 스터디룸 생성 날짜
    private List<StudyDaysEntity> studyDayEntities;


    public StRoomResponseDto(StRoomEntity entity) {

        this.title = entity.getTitle();
        this.goal = entity.getGoal();
        this.rules = entity.getRules();
        this.quest =entity.getQuest();
        this.category =entity.getCategory();
        this.intro =entity.getIntro();
        this.num = entity.getNum();
        this.mod = entity.getMod();
        this.onOff = entity.isOnOff();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.studyDayEntities =entity.getStudyDayEntities();


    }
}
//Entity의 필드 이루만 사용하므로 생성자로 Entity를 받아 필드에 값을 넣어줌
//상세정보에 노출 될 필드