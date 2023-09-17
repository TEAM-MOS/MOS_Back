package mos.mosback.web.dto;
import mos.mosback.domain.posts.StRoomEntity;
import lombok.Getter;

public class QuestionDto {
    private String question;

    public QuestionDto(StRoomEntity entity) {

        this.question = entity.getQuest();
    }
}
