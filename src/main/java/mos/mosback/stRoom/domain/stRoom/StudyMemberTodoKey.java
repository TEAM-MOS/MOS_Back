package mos.mosback.stRoom.domain.stRoom;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Embeddable
public class StudyMemberTodoKey implements Serializable {

    @Column(name = "memberID")
    private Long memberId;

    @Column(name = "todoContent")
    private String todoContent;
}