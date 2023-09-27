package mos.mosback.stRoom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class ToDoRequestDto {
    private String dayOfWeek; // 사용자가 선택한 요일
    private String todoContent;
    private boolean completed; // TodoLists 상태 ( 완료 - true, 미완료 - false)
}