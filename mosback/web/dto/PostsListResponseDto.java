package mos.mosback.web.dto;
import lombok.Getter;
import mos.mosback.domain.posts.Posts;



@Getter
public class PostsListResponseDto {
    private Long id;
    private String title;
    private String username;
    private int mod; //분위기

    private int personnel; //멤버수
    private int startDate; //시작날짜
    private int endDate; //끝나는날짜
    private boolean onOff; //진행방식 (온오프)
    private int stDay; // 스터디요일
    private String goal; //목표
    private String rule; //규칙


    public PostsListResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.username = entity.getUsername();
        this.mod = entity.getMod();
        this.personnel = entity.getPersonnel();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.onOff = entity.isOnOff();
        this.stDay = entity.getStDay();
        this.goal = entity.getGoal();
        this.rule = entity.getRule();

    }
}
