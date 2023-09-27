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

    @Column
    private int year;
    @Column
    private int month;
    @Column
    private int weekOfYear;   // 몇 주차인지 저장 (1주차, 2주차, ...)
    @Column
    private String dayOfWeek; // 선택한 요일 (월요일, 화요일, ...)



    @Builder //해당 클래스의 빌더 클래스 생성. 생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함
    public ToDoEntity(String todoContent, String dayOfWeek,int weekOfYear,int year,int month) {
        this.todoContent = todoContent;
        this.dayOfWeek = dayOfWeek;
        this.weekOfYear = weekOfYear;
        this.year = year;
        this.month = month;
    }
    public void updateToDo(String todoContent,String dayOfWeek){
        this.todoContent = todoContent;
        this.dayOfWeek = dayOfWeek;


    }


}