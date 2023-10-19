package mos.mosback.stRoom.dto;
import lombok.Getter;
import mos.mosback.domain.stRoom.ToDoEntity;
import mos.mosback.domain.stRoom.TodoStatus;

@Getter
public class StRoomToDoResponseDto {
    private String todoContent;
    private TodoStatus status;

    public StRoomToDoResponseDto(ToDoEntity entity) {
        this.todoContent = entity.getTodoContent();
        this.status=entity.getStatus();
    }
}