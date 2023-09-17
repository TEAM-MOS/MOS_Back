package mos.mosback.domain.posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter // 롬복 어노테이션
@Setter
@NoArgsConstructor // 롬복 어노테이션 (필수는 아님 그냥 코드 단순화용)
@Entity
public class StudyMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Id
    private Long roomID;

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
