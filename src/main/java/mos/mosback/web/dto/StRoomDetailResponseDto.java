package mos.mosback.web.dto;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import mos.mosback.domain.posts.StudyDaysEntity;

import java.util.Date;
import java.util.List;

public class StRoomDetailResponseDto {
    private String title;
    private String category;

    //+모집여부
    private String location;
    private Date startDate;
    private Date endDate;

    private int memberNum; //현재 멤버수
    private int maxMember; //모집 멤버수
    //+ 모집기간
    private String mod; //스터디 분위기
    //+스터디장 닉네임

    private List<StudyDaysEntity> studyDayEntities;
    private String goal;
    private String rules;
    private String intro;
    //+스터디원 사진..리스트??
    //+웨이팅멤버 사진
    //스터디 투두리스트
}
