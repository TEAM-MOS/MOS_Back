package mos.mosback.login.domain.user.dto;

public class UserInfoDto {

    private String email;
    private String password;

    // 생성자, 게터/세터 등 생략


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}