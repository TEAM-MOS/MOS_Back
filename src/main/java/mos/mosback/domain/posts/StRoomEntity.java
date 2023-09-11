package mos.mosback.domain.posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.*;

@Getter // 롬복 어노테이션
@NoArgsConstructor // 롬복 어노테이션 (필수는 아님 그냥 코드 단순화용)
@Entity //JPA 어노테이션 (주요어노테이션) : 테이블과 링크될 클래스임을 나타냄
public class StRoomEntity extends BaseTimeEntity {
    //strooms 클래스 - > 실제 DB테이블과 매칭될 클래스 ( Entity 클래스 )
    @Id //해당 테이블 PK 필드 나타냄
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK 생성규칙 나타냄 스프링 2.0은
    //GenerationType.IDENTITY 옵션을 추가해야만 auto_increment가 된다.
    private Long roomID;
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


    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "ST_ROOM_ROOMID")
    private List<StudyDaysEntity> studyDayEntities = new ArrayList<>();

    @Builder
    public StRoomEntity(String title, String goal, String rules, String quest,
                        String category, String intro, int num, String mod,
                        boolean onOff, Date startDate, Date endDate, List<StudyDaysEntity> studyDayEntities) {

        this.title = title;
        this.goal = goal;
        this.rules = rules;
        this.quest = quest;
        this.category = category;
        this.intro = intro;
        this.num = num;
        this.mod = mod;
        this.onOff = onOff;
        this.startDate = startDate;
        this.endDate = endDate;
        this.studyDayEntities = studyDayEntities;
    }


    // 다른 필요한 Getter 및 Setter 메서드

    public void update(String title, String goal, String rules, String quest,
                        String category, String intro,
                       int num, String mod, boolean onOff, Date startDate,
                       Date endDate,List<StudyDaysEntity> studyDayEntities) {
        this.title = title;
        this.goal = goal;
        this.rules = rules;
        this.quest = quest;
        this.category = category;
        this.intro = intro;
        this.num = num;
        this.mod = mod;
        this.onOff = onOff;
        this.startDate = startDate;
        this.endDate = endDate;
        this.studyDayEntities = studyDayEntities;
    }
}


