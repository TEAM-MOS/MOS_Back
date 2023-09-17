package mos.mosback.web.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import mos.mosback.data.entity.User;

import java.util.Date;

@Getter
@Setter
public class SignUpDTO {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private Date duration;
    private String message;
    private String company;

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .duration(duration)
                .message(message)
                .company(company)
                .build();
    }
}
