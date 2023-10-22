package mos.mosback.stRoom.domain.stRoom;

import lombok.Data;

import java.io.Serializable;

@Data
public class StudyMemberTodoKey implements Serializable {

    private Long memberId;
    private String todoContent;
}