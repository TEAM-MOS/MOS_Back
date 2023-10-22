package mos.mosback.login.domain.user.dto;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@NoArgsConstructor
@Getter
@Component
public class UserProfileDto {
    private String nickname;

    private String name;
    private Date str_duration;
    private Date end_duration;
    private String message;
    private String company;
    private Long roomId; // 추가된 studyRoomId 필드

    public UserProfileDto(String nickname, String name, Date str_duration, Date end_duration, String message, String company, Long roomId) {
        this.nickname = nickname;
        this.name = name;
        this.str_duration = str_duration;
        this.end_duration = end_duration;
        this.message = message;
        this.company = company;
        this.roomId = roomId;
    }
}
