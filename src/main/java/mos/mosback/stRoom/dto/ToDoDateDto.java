package mos.mosback.stRoom.dto;
import lombok.Getter;
import mos.mosback.domain.stRoom.ToDoEntity;

@Getter
public class ToDoDateDto {

    private int year;
    private int month;
    private int weekOfYear;   // 몇 주차인지 저장 (1주차, 2주차, ...)

    public ToDoDateDto(ToDoEntity entity) {

        this.year = entity.getYear();
        this.month = entity.getMonth();
        this.weekOfYear = entity.getWeekOfYear();

    }
}
