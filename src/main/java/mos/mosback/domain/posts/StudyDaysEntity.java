package mos.mosback.domain.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter // 롬복 어노테이션
@NoArgsConstructor // 롬복 어노테이션 (필수는 아님 그냥 코드 단순화용)
@Entity //JPA 어노테이션 (주요어노테이션) : 테이블과 링크될 클래스임을 나타냄
public class StudyDaysEntity implements Serializable {

    @Id //해당 테이블 PK 필드 나타냄
    @Column(name="ST_ROOM_ROOMID")
    private Long stroomid;

    @Id
    @Column(name="STUDY_DAYS")
    private String studyDays;
}