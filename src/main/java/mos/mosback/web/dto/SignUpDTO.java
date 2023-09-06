package mos.mosback.web.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import mos.mosback.data.entity.User;

@Getter
@Setter
public class SignUpDTO {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String bank;
    private String account;
    private String address;

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .bank(bank)
                .account(account)
                .address(address)
                .build();
    }
}
