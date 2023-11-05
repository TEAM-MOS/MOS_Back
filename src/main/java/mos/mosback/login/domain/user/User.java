package mos.mosback.login.domain.user;


import lombok.*;
import mos.mosback.stRoom.domain.stRoom.StRoomEntity;
import mos.mosback.stRoom.domain.stRoom.StudyMemberEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "USERS")
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")

    private Long id;

    @Column(name ="room_id")
    private Long roomId;

    @OneToMany
    private List<StRoomEntity> stRooms= new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<StudyMemberEntity> studyMemberships = new ArrayList<>();

    public List<StudyMemberEntity> getStudyMemberships() {
        return studyMemberships;
    }
    public void addStudyMembership(StudyMemberEntity studyMember) {
        this.studyMemberships.add(studyMember);
        studyMember.setUser(this);
    }

    private String email; // 이메일
    private String password; // 비밀번호

    private String name;

    private String nickname; // 닉네임


    @Column(name = "image_url")
    private String imageUrl; // 프로필 이미지
    private Date str_duration;

    private Date end_duration;
    private String message;

    private String company;

    private String tend1;
    private String tend2;


    public String getTend1() {
        return tend1;
    }

    public void setTend1(String tend1) {
        this.tend1 = tend1;
    }

    public String getTend2() {
        return tend2;
    }

    public void setTend2(String tend2) {
        this.tend2 = tend2;
    }


    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

    private String refreshToken; // 리프레시 토큰


    // 유저 권한 설정 메소드
    public void authorizeUser() {
        this.role = Role.USER;
    }

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    //==setter==//
    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }





    public void setId(Long id) {
        this.id = id;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {this.name = name;}

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getStr_duration() {
        return str_duration;
    }

    public void setStr_duration(Date str_duration) {
        this.str_duration = str_duration;
    }

    public Date getEnd_duration() {
        return end_duration;
    }

    public void setEnd_duration(Date end_duration) {
        this.end_duration = end_duration;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    //== 유저 필드 업데이트 ==//
    public void updateNickname(String updateNickname) {
        this.nickname = updateNickname;
    }

    public void updateStr_duration(Date updateStr_Duration) {
        this.str_duration = updateStr_Duration;
    }

    public void updateEnd_duration(Date updateEnd_duration) {
        this.end_duration = updateEnd_duration;
    }

    public void updateMessage(String updateMessage) {
        this.message = updateMessage;
    }

    public void updateCompany(String updateCompany) {
        this.company = updateCompany;
    }


    public void updatePassword(String updatePassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(updatePassword);
    }

    public void updatePw(String password) {
        this.password = cryptopassword(password);
    }
    public String cryptopassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        return encodedPassword;
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }
}