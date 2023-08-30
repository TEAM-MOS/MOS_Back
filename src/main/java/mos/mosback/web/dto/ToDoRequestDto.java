package mos.mosback.web.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mos.mosback.domain.posts.ToDoEntity;


@Getter
@NoArgsConstructor
public class ToDoRequestDto {

    Long TodoId;
    private String todoContent;
    private boolean completed; // TodoLists 상태 ( 완료 - true, 미완료 - false)

    @Builder
    public ToDoRequestDto(String todoContent, boolean completed) {
        this.todoContent = todoContent;
        this.completed = completed;
    }

    public ToDoEntity toEntity() {
        return ToDoEntity.builder()
                .todoContent(todoContent)
                .completed(completed)
                .build();
    }

}
