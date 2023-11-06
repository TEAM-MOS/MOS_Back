package mos.mosback.login.domain.user.dto;

public class UserInfo {

    private String email;
    private String password;
    private String nickname;

    // 생성자, 게터/세터 등 생략

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname(){
        return nickname;
    }
}