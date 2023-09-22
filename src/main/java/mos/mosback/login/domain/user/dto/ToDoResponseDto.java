package mos.mosback.web.dto;
import lombok.Getter;
import mos.mosback.domain.posts.ToDoEntity;

@Getter
public class ToDoResponseDto {
    private Long TodoId;
    private String todoContent;
    private boolean completed;

    public ToDoResponseDto(ToDoEntity entity) {

        this.todoContent = getTodoContent();
        this.completed = isCompleted();
    }
}