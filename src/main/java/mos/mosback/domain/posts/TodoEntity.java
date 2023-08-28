package mos.mosback.domain.posts;
import lombok.*;
import lombok.AllArgsConstructor;
import mos.mosback.domain.posts.BaseTimeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter // 롬복 어노테이션
@NoArgsConstructor
@AllArgsConstructor
@Entity //JPA 어노테이션 (주요어노테이션) : 테이블과 링크될 클래스
public class TodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TodoId; //유저 투두리스트

    @Column(nullable = false)
    private String TodoContent;

    @Column(nullable = false)
    private Boolean completed;

}
