package mos.mosback.stRoom.dto;
import lombok.Getter;
import mos.mosback.domain.stRoom.ToDoEntity;

@Getter
public class ToDoResponseDto {

    private String todoContent;
    private boolean completed;

    public ToDoResponseDto(ToDoEntity entity) {

        this.todoContent = getTodoContent();
        this.completed = isCompleted();
    }
}