package mos.mosback.domain.posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter // 롬복 어노테이션
@NoArgsConstructor
@Entity //JPA 어노테이션 (주요어노테이션) : 테이블과 링크될 클래스
public class ToDoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TodoIdx;

    @Column
    private String todoContent;

    @Column
    private boolean completed;
    // TodoLists 상태 ( 완료 - true, 미완료 - false)

    @Builder //해당 클래스의 빌더 클래스 생성. 생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함
    public ToDoEntity(String todoContent, Boolean completed) {
        this.todoContent = todoContent;
        this.completed = completed;
    }
    public void updateToDo(String todoContent,boolean completed){
        this.todoContent = todoContent;
        this.completed = completed;
    }


}