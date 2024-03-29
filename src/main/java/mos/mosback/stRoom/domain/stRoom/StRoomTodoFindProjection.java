package mos.mosback.stRoom.domain.stRoom;

import java.time.LocalDate;

public interface StRoomTodoFindProjection {
    Long getRoomId();
    LocalDate getDate();
    String getTodoContent();

    Long getIdx();
    TodoStatus getStatus();
}