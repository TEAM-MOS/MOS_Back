package mos.mosback.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginDTO {
    private String email;
    private String password;

}
