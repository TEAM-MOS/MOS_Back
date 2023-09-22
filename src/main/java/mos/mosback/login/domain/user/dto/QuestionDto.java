package mos.mosback.login.domain.user.dto;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mos.mosback.domain.posts.StRoomEntity;
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
