package mos.mosback.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserUpdateDTO {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private Date duration;
    private String message;
    private String company;
}
