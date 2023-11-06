package mos.mosback.stRoom.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class StudyMemberRoomInfoResponseDto {
    private double studyRoomTodoAverage; // 평균값
    private List<StRoomMemberToDoResponseDto> todoList; // 사용자 투두정보
    private List<StudyRoomDayDto> roomDayList;
}