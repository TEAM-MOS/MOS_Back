package mos.mosback.stRoom.domain.stRoom;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mos.mosback.login.domain.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Getter // 롬복 어노테이션
@Setter
@NoArgsConstructor // 롬복 어노테이션 (필수는 아님 그냥 코드 단순화용)
@Entity //JPA 어노테이션 (주요어노테이션) : 테이블과 링크될 클래스임을 나타냄
public class StRoomEntity extends BaseTimeEntity {
    //strooms 클래스 - > 실제 DB테이블과 매칭될 클래스 ( Entity 클래스 )
    @Id //해당 테이블 PK 필드 나타냄
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK 생성규칙 나타냄 스프링 2.0은
    //GenerationType.IDENTITY 옵션을 추가해야만 auto_increment가 된다.
    private Long roomId;
    //웬만하면 Entity의 Pk는 long타입의 auto_increment 쓰는게 좋음
    @Column(length = 500, nullable = false)
    private String title;

    @Column(nullable = false)
    private String goal; //스터디 목표
    @Column(nullable = false)
    private String rules; //스터디 규칙
    @Column(nullable = false)
    private String quest; //생성 시 질문
    @Column(nullable = false)
    private String category; // 스터디 카테고리
    private String intro; //스터디 소개
    private int memberNum; //현재 멤버수
    private int maxMember; //모집 멤버수
    private String mod; //스터디 분위기
    private int click;// 클릭횟수 (인기순 조회)
    private boolean onOff; //진행방식 (온오프)
    private String location;
    private int online; //온라인
    private LocalDate startDate; //스터디 시작 날짜
    private LocalDate endDate; //스터디 끝나는 날짜
    private boolean recruiting; //모집여부
    private LocalDate deadline; // 모집 마감날짜

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<StudyDaysEntity> studyDayEntities = new ArrayList<>();


    //----추가-----
    public Long getRoomId() {
        return roomId;
    }

    public String getTitle() {
        return title;
    }


    @JsonManagedReference
    @OneToMany(mappedBy = "stRoom")
    private List<StudyMemberEntity> members = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "created_by_user_email")
    private User createdByUser;


    //----------------

    @Builder
    public StRoomEntity(String title, String goal, String rules, String quest,
                        String category, String intro, int memberNum, int maxMember
                         ,String mod, boolean onOff, String location,int online,
                        LocalDate startDate, LocalDate endDate, List<StudyDaysEntity> studyDayEntities
    ) {
        this.title = title;
        this.goal = goal;
        this.rules = rules;
        this.quest = quest;
        this.category = category;
        this.intro = intro;
        this.memberNum = memberNum;
        this.maxMember = maxMember;
        this.mod = mod;
        this.onOff = onOff;
        this.location = location;
        this.online = online;
        this.startDate = startDate;
        this.endDate = endDate;
        this.studyDayEntities = studyDayEntities;
    }


    public void update(String title, String goal, String rules, String quest,
                       String category, String intro, int maxMember,
                       String mod, boolean onOff,String location,int online,LocalDate startDate,
                       LocalDate endDate,List<StudyDaysEntity> studyDayEntities) {
        this.title = title;
        this.goal = goal;
        this.rules = rules;
        this.quest = quest;
        this.category = category;
        this.intro = intro;
        this.maxMember = maxMember;
        this.mod = mod;
        this.onOff = onOff;
        this.location = location;
        this.online = online;
        this.startDate = startDate;
        this.endDate = endDate;
        this.studyDayEntities = studyDayEntities;
    }


}