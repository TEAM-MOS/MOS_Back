package mos.mosback.domain.posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter // 롬복 어노테이션
@NoArgsConstructor // 롬복 어노테이션 (필수는 아님 그냥 코드 단순화용)
@Entity //JPA 어노테이션 (주요어노테이션) : 테이블과 링크될 클래스임을 나타냄
public class Posts extends BaseTimeEntity {
    //Posts 클래스 - > 실제 DB테이블과 매칭될 클래스 ( Entity 클래스 )
    @Id //해당 테이블 PK 필드 나타냄
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK 생성규칙 나타냄 스프링 2.0은
    //GenerationType.IDENTITY 옵션을 추가해야만 auto_increment가 된다.
    private Long groupID;
    //웬만하면 Entity의 Pk는 long타입의 auto_increment 쓰는게 좋음
    @Column(length = 500, nullable = false)
    private String title;

    @Column(nullable = false)
    private String goal; //스터디 목표
    private String rules; //스터디 규칙
    private String quest; //생성 시 질문
    private String category; // 스터디 카테고리
    private String intro; //스터디 소개
    private int num; //멤버수
    private String mod; //스터디 분위기
    private int click;// 클릭횟수 (인기순 조회)
    private boolean onOff; //진행방식 (온오프)
    private Date startDate; //스터디 시작 날짜
    private Date endDate; //스터디 끝나는 날짜
    private Date rcstart; // 모집 시작 날짜
    private Date rcend; //모집 마감 날짜

    @ElementCollection
    @Enumerated(EnumType.STRING) // 열거형 값의 매핑 타입을 지정
    private Set<StudyDays> studyDays;

    @Builder //해당 클래스의 빌더 클래스 생성. 생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함
    public Posts(String title, String goal, String rules, String quest,Set<StudyDays> studyDays,
                 String category, String date, String intro, int num, String mod,
                 boolean onOff, Date startDate, Date endDate, Date rcstart, Date rcend) {
        this.title = title;
        this.goal = goal;
        this.rules = rules;
        this.quest = quest;
        this.studyDays = studyDays;
        this.category = category;
        this.intro = intro;
        this.mod = mod;
        this.num = num;
        this.onOff = onOff;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rcstart = rcstart;
        this.rcend = rcend;
    }
    public void update(String title, String goal, String rules, String quest, Set<StudyDays> studyDays,
                       String category, String intro, int num, String mod,
                       boolean onOff, Date startDate, Date endDate, Date rcstart, Date rcend) {
        this.title = title;
        this.goal = goal;
        this.rules = rules;
        this.quest = quest;
        this.studyDays = studyDays;
        this.category = category;
        this.intro = intro;
        this.mod = mod;
        this.num = num;
        this.onOff = onOff;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rcstart = rcstart;
        this.rcend = rcend;
    }


}