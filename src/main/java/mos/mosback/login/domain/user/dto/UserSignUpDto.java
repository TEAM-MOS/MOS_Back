package mos.mosback.login.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Getter
public class UserSignUpDto {

    private String email;
    private String password;

}