package mos.mosback.stRoom.dto;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
@NoArgsConstructor
public class QuestionAndAnswerResponseDto {
    private Long memberId;
    private String question;
    private String answer;

}
