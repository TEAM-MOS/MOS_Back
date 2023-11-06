package mos.mosback.stRoom.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mos.mosback.stRoom.domain.stRoom.TodoStatus;


@Setter
@Getter
@NoArgsConstructor
public class stRoomToDoRequestDto {

    private String todoContent;
    private TodoStatus status;
    private Long roomId;
}