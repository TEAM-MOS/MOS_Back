package mos.mosback.login.domain.user.dto;

import java.util.Date;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;


@NoArgsConstructor
@Getter
@Component
public class UserProfileDto {

    @Column(nullable = false)
    private String nickname;
    private String name;
    private Date str_duration;
    private Date end_duration;
    private String message;
    private String company;

    private String tend1;
    private String tend2;

    private Long roomId;




    public UserProfileDto(String nickname, String name, Date str_duration,
                          Date end_duration, String message, String company, String tend1, String tend2, Long roomId) {
        this.nickname = nickname;
        this.name = name;
        this.str_duration = str_duration;
        this.end_duration = end_duration;
        this.message = message;
        this.company = company;
        this.roomId = roomId;
        this.tend1 = tend1;
        this.tend2= tend2;


    }
    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

}

