package mos.mosback.domain.stRoom;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter // 롬복 어노테이션
@Setter
@NoArgsConstructor
@Entity
@IdClass(StudyMemberTodoEntity.class)
public class StudyMemberTodoEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberID")
    private Long memberId;

    @Id
    @Column
    private String todoContent;

    @ManyToOne
    @JoinColumn(name = "roomID")
    private StRoomEntity stRoom;

    @Id
    @ManyToOne
    @JoinColumn(name = "todoId")
    private ToDoEntity toDoEntity;

    // 다른 필드와 매핑
    @Enumerated(EnumType.STRING)
    private TodoStatus status;


    public void update(String todoContent, TodoStatus status) {
        this.todoContent = todoContent;
        this.status = status;
    }

}