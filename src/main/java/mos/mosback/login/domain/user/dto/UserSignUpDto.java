package mos.mosback.login.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Getter
public class UserSignUpDto {

    private String email;
    private String password;
<<<<<<< Updated upstream

=======
    private String nickname;
    private Date duration;
    private String message;
    private String company;
>>>>>>> Stashed changes
}