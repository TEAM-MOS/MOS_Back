package mos.mosback.domain.stRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter // 롬복 어노테이션
@NoArgsConstructor
@Entity //JPA 어노테이션 (주요어노테이션) : 테이블과 링크될 클래스
public class ToDoEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    @ManyToOne
    @JoinColumn(name = "roomID")
    private StRoomEntity stRoom;

    @Column
    private String todoContent;

    public void update(String todoContent)
    {
        this.todoContent = todoContent;
    }



// 스터디 방에 대한 Todo 정보를 담는 엔티티

}