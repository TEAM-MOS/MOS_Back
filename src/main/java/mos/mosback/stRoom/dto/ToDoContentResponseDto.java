package mos.mosback.stRoom.dto;
import lombok.Getter;
import mos.mosback.domain.stRoom.ToDoEntity;

@Getter
public class ToDoContentResponseDto {
    private String todoContent;

    public ToDoContentResponseDto(ToDoEntity entity) {
        this.todoContent = entity.getTodoContent();
    }
}