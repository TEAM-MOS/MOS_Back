package mos.mosback.stRoom.domain.stRoom;
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
    @JoinColumn(name = "roomId")
    private StRoomEntity stRoom;

    @Column
    private String todoContent;

    @Enumerated(EnumType.STRING)
    private TodoStatus status;

    public void update(String todoContent, TodoStatus status)
    {

        this.todoContent = todoContent;
        this.status = status;
    }



// 스터디 방에 대한 투두 정보를 담는 엔티티

}