package mos.mosback.stRoom.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mos.mosback.domain.stRoom.StudyMemberTodoEntity;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class StudyMemberRoomInfoResponseDto {
    private double studyRoomTodoAverage; // 평균값
    private List<StRoomMemberToDoResponseDto> todoList; // 사용자 TODO 정보
    private List<StudyRoomDayDto> roomDayList;
}