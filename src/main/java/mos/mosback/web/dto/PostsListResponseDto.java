package mos.mosback.web.dto;
import lombok.Getter;
import mos.mosback.domain.posts.Posts;

import java.util.Date;


@Getter
public class PostsListResponseDto {
    Long groupID;
    private String title;
    private String goal; //스터디 목표
    private String rules; //스터디 규칙
    private String tend; //유저 스터디 성향
    private String category; // 스터디 카테고리
    private String date; //스터디 요일
    private String intro; //스터디 소개
    private int num; //멤버수
    private String mod; //스터디 분위기
    private int click;// 클릭횟수 (인기순 조회)
    private boolean onOff; //진행방식 (온오프)
    private Date startDate; //스터디 시작 날짜
    private Date endDate; //스터디 끝나는 날짜
    private Date createDate; // 스터디룸 생성 날짜
    private Date rcstart; // 모집 시작 날짜
    private Date rcend; //모집 마감 날짜


    public PostsListResponseDto(Posts entity) {

        this.title = entity.getTitle();
        this.goal = entity.getGoal();
        this.rules = entity.getRules();
        this.tend =entity.getTend();
        this.category =entity.getCategory();
        this.date =entity.getDate();
        this.intro =entity.getIntro();
        this.num = entity.getNum();
        this.mod = entity.getMod();
        this.onOff = entity.isOnOff();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.rcstart =entity.getRcstart();
        this.rcend =entity.getRcend();

    }
}
