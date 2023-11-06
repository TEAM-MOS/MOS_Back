package mos.mosback.stRoom.dto;

import lombok.Data;

import java.util.Date;

@Data
public class StudyRoomDayDto {
    private Date date; // 날짜 Date 객체
    private String dayOfWeek; // "월" ~ "일" 값
    private int dayVal; // 날짜 값
}
