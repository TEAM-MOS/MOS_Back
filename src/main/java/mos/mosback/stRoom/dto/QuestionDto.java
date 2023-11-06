package mos.mosback.stRoom.dto;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mos.mosback.stRoom.domain.stRoom.StRoomEntity;
import lombok.Getter;

@Setter
@Getter
@NoArgsConstructor
public class QuestionDto {
    private String question;

    public QuestionDto(StRoomEntity entity) {

        this.question = entity.getQuest();
    }
}
