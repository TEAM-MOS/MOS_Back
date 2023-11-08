package mos.mosback.stRoom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mos.mosback.stRoom.domain.stRoom.TodoStatus;

import java.time.LocalDate;


@Setter
@Getter
@NoArgsConstructor
public class StudyMemberToDoRequestDto {
    private String todoContent;
    private TodoStatus status;
    private Long roomId;
    private Long todoId;
    private String currentEmail;
    private LocalDate date;

}