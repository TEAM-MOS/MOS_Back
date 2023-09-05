package mos.mosback.web.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mos.mosback.domain.posts.Posts;

import javax.persistence.Column;
import java.util.Date;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {
    Long groupID;
    private String title;
    private String goal; //스터디 목표
    private String rules; //스터디 규칙
    private String quest; //생성 시 질문
    private String tend; //유저 스터디 성향
    private String category; // 스터디 카테고리
    private String date; //스터디 요일
    private String intro; //스터디 소개
    private int num; //멤버수
    private String mod; //스터디 분위기
    private boolean onOff; //진행방식 (온오프)
    private Date startDate; //스터디 시작 날짜
    private Date endDate; //스터디 끝나는 날짜
    private Date rcstart; // 모집 시작 날짜
    private Date rcend; //모집 마감 날짜



    @Builder
    public PostSaveRequestDto(String title, String goal, String rules, String quest, String tend,
                              String category, String date, String intro, int num, String mod,
                              boolean onOff, Date startDate, Date endDate, Date rcstart, Date rcend) {
        this.title = title;
        this.goal = goal;
        this.rules = rules;
        this.quest = quest;
        this.tend = tend;
        this.category = category;
        this.date = date;
        this.intro = intro;
        this.mod = mod;
        this.num = num;
        this.onOff = onOff;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rcstart = rcstart;
        this.rcend = rcend;
    }

    public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .goal(goal)
                .rules(rules)
                .quest(quest)
                .tend(tend)
                .category(category)
                .date(date)
                .intro(intro)
                .mod(mod)
                .num(num)
                .date(date)
                .intro(intro)
                .mod(mod)
                .num(num)
                .onOff(onOff)
                .startDate(startDate)
                .endDate(endDate)
                .rcstart(rcstart)
                .rcend(rcend)
                .build();
    }
}
