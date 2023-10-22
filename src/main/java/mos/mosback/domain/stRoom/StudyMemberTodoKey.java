package mos.mosback.domain.stRoom;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Data
public class StudyMemberTodoKey implements Serializable {

    private Long memberId;
    private String todoContent;
}