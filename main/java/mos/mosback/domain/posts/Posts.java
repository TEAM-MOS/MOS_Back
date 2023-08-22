package mos.mosback.domain.posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mos.mosback.domain.posts.BaseTimeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter // 롬복 어노테이션
@NoArgsConstructor // 롬복 어노테이션 (필수는 아님 그냥 코드 단순화용)
@Entity //JPA 어노테이션 (주요어노테이션) : 테이블과 링크될 클래스임을 나타냄
public class Posts extends BaseTimeEntity {
    //Posts 클래스 - > 실제 DB테이블과 매칭될 클래스 ( Entity 클래스 )
    @Id //해당 테이블 PK 필드 나타냄
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK 생성규칙 나타냄 스프링 2.0은
    //GenerationType.IDENTITY 옵션을 추가해야만 auto_increment가 된다.
    private Long id;
    //웬만하면 Entity의 Pk는 long타입의 auto_increment 쓰는게 좋음
    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String username;
    private String goal;
    private String rule;

    @Column(columnDefinition = "INT", nullable = false)

    private int mod; //스터디 분위기
    private int personnel; //멤버수
    private int startDate; //시작날짜
    private int endDate; //끝나는날짜
    private int stDay; // 스터디요일

    @Column(columnDefinition = "BOOLEAN", nullable = false)

    private boolean onOff; //진행방식 (온오프)

    @Builder //해당 클래스의 빌더 클래스 생성. 생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함
    public Posts(String title, String username,
                 String goal, String rule, int mod,
                 int personnel, int startDate,
                 int endDate,boolean onOff,int stDay) {

        this.title = title;
        this.username = username;
        this.mod = mod ;
        this.goal = goal;
        this.rule = rule;
        this.personnel = personnel;
        this.startDate= startDate;
        this.endDate = endDate;
        this.onOff = onOff;
        this.stDay = stDay;

    }
    public void update(String title, String username, int mod,
                       String goal, String rule,int personnel, int startDate,
                       int endDate,boolean onOff,int stDay) {
        this.title = title;
        this.username = username;
        this.mod = mod;
        this.goal = goal;
        this.rule = rule;
        this.personnel =personnel;
        this.startDate= startDate;
        this.endDate = endDate;
        this.onOff = onOff;
        this.stDay = stDay;

    }


}