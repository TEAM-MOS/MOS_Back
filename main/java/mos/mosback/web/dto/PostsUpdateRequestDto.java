package mos.mosback.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private int mod; //분위기

    private int personnel; //멤버수
    private int startDate; //시작날짜
    private int endDate; //끝나는날짜
    private boolean onOff; //진행방식 (온오프)
    private int stDay; // 스터디요일
    private String goal; //목표
    private String rule; //규칙


    @Builder
    public PostsUpdateRequestDto(String title, String content, String username, int mod,
                                 int personnel, int startDate, int endDate, boolean onOff,
                                 int stDay, String goal, String rule){
        this.title = title;
        this.content = content;
        this.username = username;
        this.mod = mod;
        this.personnel = personnel;
        this.startDate = startDate;
        this.endDate = endDate;
        this.onOff = onOff;
        this.stDay = stDay;
        this.goal = goal;
        this.rule = rule;    }
}
