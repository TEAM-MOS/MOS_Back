package mos.mosback.stRoom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class StudyMemberToDoRequestDto {
    private String todoContent;
    private boolean completed; // TodoLists 상태 ( 완료 - true, 미완료 - false)
    private Long roomID;
    private Long todoId;
    private String currentEmail;
}