package mos.mosback.stRoom.domain.stRoom;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mos.mosback.login.domain.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter // 롬복 어노테이션
@Setter
@NoArgsConstructor
@Entity
public class StudyMemberEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberID")
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getUserId() {
        return this.user.getId();
    }

//    @ManyToMany
//    @JoinTable(name = "study_member_room", // 조인 테이블 이름 설정
//            joinColumns = @JoinColumn(name = "memberID"), // 현재 엔티티의 PK 컬럼
//            inverseJoinColumns = @JoinColumn(name = "roomId")) // 연관 엔티티의 PK 컬럼
//    private List<StRoomEntity> rooms = new ArrayList<>();


    @ManyToMany(mappedBy = "members")
    private List<StRoomEntity> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<StudyMemberRoomEntity> studyMemberRooms = new ArrayList<>();

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "roomId")
    private StRoomEntity stRoom;

    // 다른 필드와 매핑

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Column
    private String answer; // 스터디 답변

    @Column
    private LocalDateTime joinedAt;


    public StudyMemberRoomEntity getMemberRoom() {
        return this.studyMemberRooms.get(0);
    }

    public void setStudyMemberRoom(StudyMemberRoomEntity savedStudyMemberRoom) {
    }
}