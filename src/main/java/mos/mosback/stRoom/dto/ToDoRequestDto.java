package mos.mosback.stRoom.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mos.mosback.domain.stRoom.ToDoEntity;


@Getter
@NoArgsConstructor
public class ToDoRequestDto {

    private int weekOfYear;
    private String dayOfWeek; // 사용자가 선택한 요일
    private String todoContent;
    private boolean completed; // TodoLists 상태 ( 완료 - true, 미완료 - false)

    @Builder
    public ToDoRequestDto(String todoContent, boolean completed,String dayOfWeek,int weekOfYear) {
        this.weekOfYear = weekOfYear;
        this.dayOfWeek = dayOfWeek;
        this.todoContent = todoContent;
        this.completed = completed;
    }

    public ToDoEntity toEntity() {
        return ToDoEntity.builder()
                .weekOfYear(weekOfYear)
                .dayOfWeek(dayOfWeek)
                .todoContent(todoContent)
                .completed(completed)
                .build();
    }

}