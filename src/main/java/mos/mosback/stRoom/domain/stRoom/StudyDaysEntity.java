package mos.mosback.stRoom.domain.stRoom;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter // 롬복 어노테이션
@NoArgsConstructor // 롬복 어노테이션 (필수는 아님 그냥 코드 단순화용)
@Entity //JPA 어노테이션 (주요어노테이션) : 테이블과 링크될 클래스임을 나타냄
public class StudyDaysEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dayIdx;
    @Column(name = "STUDY_DAYS")
    private String studyDays;

}