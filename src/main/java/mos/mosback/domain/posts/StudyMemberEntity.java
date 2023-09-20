package mos.mosback.domain.posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter // 롬복 어노테이션
@Setter
@NoArgsConstructor
@Entity
public class StudyMemberEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberID")
    private Long memberId;

    @JoinColumn(name = "roomID")
    private Long roomId;

    // 다른 필드와 매핑

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Column
    private String answer; // 스터디 답변


    @Builder
    public StudyMemberEntity(MemberStatus status){
        this.status = status;
    }

}
