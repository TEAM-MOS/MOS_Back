package mos.mosback.stRoom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mos.mosback.domain.stRoom.TodoStatus;


@Setter
@Getter
@NoArgsConstructor
public class StudyMemberToDoRequestDto {
    private String todoContent;
    private TodoStatus status;
    private Long roomID;
    private Long todoId;
    private String currentEmail;

}