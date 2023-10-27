package mos.mosback.stRoom.dto;

import lombok.Data;
import mos.mosback.stRoom.domain.stRoom.StudyMemberTodoEntity;
import mos.mosback.stRoom.domain.stRoom.TodoStatus;

@Data
public class StRoomMemberToDoResponseDto {
    private Long idx;
    private String todoContent;
    private TodoStatus status;


    public StRoomMemberToDoResponseDto (StudyMemberTodoEntity entity) {
        this.idx = entity.getIdx();
        this.status = entity.getStatus();
        this.todoContent = entity.getTodoContent();
    }
}
