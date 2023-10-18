package mos.mosback.stRoom.dto;
import lombok.Getter;
import mos.mosback.domain.stRoom.ToDoEntity;

@Getter
public class StRoomToDoResponseDto {
    private String todoContent;

    public StRoomToDoResponseDto(ToDoEntity entity) {
        this.todoContent = entity.getTodoContent();
    }
}