package mos.mosback.login.domain.user.dto;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Getter
public class UserProfileDto {
    private String nickname;

    private String name;
    private Date str_duration;
    private Date end_duration;
    private String message;
    private String company;
}
