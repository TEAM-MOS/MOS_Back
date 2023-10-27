package mos.mosback.stRoom.domain.stRoom;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter // 롬복 어노테이션
@Setter
@NoArgsConstructor
@Entity
public class StudyMemberTodoEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "memberID")
    private Long memberId;

    @Column(name = "todoContent")
    private String todoContent;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private StRoomEntity stRoom;

    // 다른 필드와 매핑
    @Enumerated(EnumType.STRING)
    private TodoStatus status;


    public void update(String todoContent, TodoStatus status) {
        this.todoContent = todoContent;
        this.status = status;
    }

}