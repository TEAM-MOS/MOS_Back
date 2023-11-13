package mos.mosback.login.domain.user.dto;

import java.util.Date;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@NoArgsConstructor
@Getter
@Component
public class UserProfileDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String nickname;
    private String name;
    private Date str_duration;
    private Date end_duration;
    private String message;
    private String company;

    private String tend1;
    private String tend2;


    @Column(name = "image_url")
    private String imageUrl;


    public UserProfileDto(Long id,String nickname, String name, Date str_duration,
                          Date end_duration, String message,
                          String company, String tend1, String tend2, String imageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.name = name;
        this.str_duration = str_duration;
        this.end_duration = end_duration;
        this.message = message;
        this.company = company;
        this.tend1 = tend1;
        this.tend2= tend2;
        this.imageUrl=imageUrl;


    }
     public Long getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}


