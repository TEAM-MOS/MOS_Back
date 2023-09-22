package mos.mosback.login.domain.user.dto;
import mos.mosback.login.domain.user.User;
import lombok.Getter;


@Getter
public class Home_nickResponseDto {
    private String nickname;

    public Home_nickResponseDto(User entity) {
        this.nickname = entity.getNickname();
    }
}