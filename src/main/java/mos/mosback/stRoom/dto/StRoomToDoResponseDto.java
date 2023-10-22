package mos.mosback.stRoom.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mos.mosback.stRoom.domain.stRoom.ToDoEntity;
import mos.mosback.stRoom.domain.stRoom.TodoStatus;

@Setter
@Getter
@NoArgsConstructor
public class StRoomToDoResponseDto {
    private String todoContent;
    private TodoStatus status;

    public StRoomToDoResponseDto(ToDoEntity entity) {
        this.todoContent = entity.getTodoContent();
        this.status=entity.getStatus();
    }
}