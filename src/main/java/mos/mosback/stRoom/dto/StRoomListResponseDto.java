package mos.mosback.stRoom.dto;
import lombok.Getter;
import mos.mosback.stRoom.domain.stRoom.StRoomEntity;
import mos.mosback.stRoom.domain.stRoom.StudyDaysEntity;

import java.time.LocalDate;
import java.util.List;


@Getter
public class StRoomListResponseDto {

    private String title;
    private String goal; //스터디 목표
    private String rules; //스터디 규칙
    private String quest;
    private String category; // 스터디 카테고리
    private String intro; //스터디 소개
    private int memberNum; //멤버수
    private int maxMember;
    private String mod; //스터디 분위기
    private int click;// 클릭횟수 (인기순 조회)
    private boolean onOff; //진행방식 (온오프)
    private LocalDate startDate; //스터디 시작 날짜
    private LocalDate endDate; //스터디 끝나는 날짜
    private String location;
    private int online; //온라인

    private List<StudyDaysEntity> studyDayEntities;

    public StRoomListResponseDto(StRoomEntity entity) {

        this.title = entity.getTitle();
        this.goal = entity.getGoal();
        this.rules = entity.getRules();
        this.quest =entity.getQuest();
        this.category =entity.getCategory();
        this.intro =entity.getIntro();
        this.memberNum = entity.getMemberNum();
        this.maxMember = entity.getMaxMember();
        this.mod = entity.getMod();
        this.onOff = entity.isOnOff();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.studyDayEntities = entity.getStudyDayEntities();
    }
    public StRoomListResponseDto(Home_RoomResponseDto homeRoomResponseDto) {
        this.title = homeRoomResponseDto.getTitle();
        this.location = homeRoomResponseDto.getLocation();
        this.online = homeRoomResponseDto.getOnline();
        this.category = homeRoomResponseDto.getCategory();
        this.memberNum = homeRoomResponseDto.getMemberNum();
        this.maxMember = homeRoomResponseDto.getMaxMember();
    }
}