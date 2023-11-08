package mos.mosback.stRoom.dto;
import lombok.Getter;
import lombok.Setter;
import mos.mosback.stRoom.domain.stRoom.TodoStatus;

@Getter
@Setter
public class MemberTodoResponseDto {
    private String todoContent;
    private Long idx;
    private TodoStatus status;

}
