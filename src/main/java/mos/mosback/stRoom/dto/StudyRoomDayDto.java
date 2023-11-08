package mos.mosback.stRoom.dto;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
;

@Data
public class StudyRoomDayDto {
    private int year;
    private int month;
    private String dayOfWeek; // "월" ~ "일" 값
    private int dayVal; // 날짜 값
}
