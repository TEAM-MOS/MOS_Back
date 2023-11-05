package mos.mosback.stRoom.domain.stRoom;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mos.mosback.login.domain.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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
}
