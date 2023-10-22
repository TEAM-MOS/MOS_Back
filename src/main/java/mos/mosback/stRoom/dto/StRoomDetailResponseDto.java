package mos.mosback.stRoom.dto;
import mos.mosback.stRoom.domain.stRoom.StudyDaysEntity;

import java.time.LocalDate;
import java.util.List;

public class StRoomDetailResponseDto {
    private String title;
    private String category;
    private boolean recruiting;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private int memberNum; //현재 멤버수
    private int maxMember; //모집 멤버수

    private String mod; //스터디 분위기

    private String leader; //스터디리더 닉네임

    private List<StudyDaysEntity> studyDayEntities;
    private String goal;
    private String rules;
    private String intro;
    //+스터디원 사진
    //+스터디룸 투두리스트
}
