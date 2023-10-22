package mos.mosback.stRoom.dto;

import lombok.Data;
import mos.mosback.stRoom.domain.stRoom.StudyMemberTodoEntity;
import mos.mosback.stRoom.domain.stRoom.TodoStatus;

@Data
public class StRoomMemberToDoResponseDto {
    private Long todoId;
    private String todoContent;
    private TodoStatus status;


    public StRoomMemberToDoResponseDto (StudyMemberTodoEntity entity) {
        this.todoId = entity.getToDoEntity().getTodoId();
        this.status = entity.getStatus();
        this.todoContent = entity.getTodoContent();
    }
}
