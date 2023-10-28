package mos.mosback.login.domain.user.service;

import mos.mosback.login.domain.user.Role;
import mos.mosback.login.domain.user.User;
import mos.mosback.login.domain.user.dto.FindPWDto;
import mos.mosback.login.domain.user.dto.MailDto;

import mos.mosback.login.domain.user.dto.UserProfileDto;
import mos.mosback.login.domain.user.dto.UserSignUpDto;
import mos.mosback.login.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import mos.mosback.stRoom.domain.stRoom.StRoomEntity;
import mos.mosback.stRoom.domain.stRoom.StudyMemberEntity;
import mos.mosback.stRoom.dto.StRoomSaveRequestDto;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender mailSender;

    public void signUp(UserSignUpDto userSignUpDto) throws Exception {

        if (userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .email(userSignUpDto.getEmail())
                .password(userSignUpDto.getPassword())
                .role(Role.GUEST)
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
    }



    public User getUserByEmail(String email) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new Exception("현재 로그인한 사용자를 찾을 수 없습니다.");
        }
    }

    //마이페이지정보입력
    public void createUser(String currentEmail, UserProfileDto userProfileDto) throws Exception {
        if (userRepository.findByNickname(userProfileDto.getNickname()).isPresent()) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        try {
            User user = getUserByEmail(currentEmail);

            // 회원 정보 생성
            user.setNickname(userProfileDto.getNickname());
            user.setStr_duration(userProfileDto.getStr_duration());
            user.setEnd_duration(userProfileDto.getEnd_duration());
            user.setMessage(userProfileDto.getMessage());
            user.setCompany(userProfileDto.getCompany());

            user.setRole(Role.USER);
            userRepository.save(user);
        } catch (Exception e) {
            throw new Exception("회원 정보를 생성하는 동안 오류가 발생했습니다.", e);
        }
    }

    //마이페이지 업데이트
    public void updateUserProfile(String currentEmail, UserProfileDto userProfileDto) throws Exception {
        try {
            User user = getUserByEmail(currentEmail);

            // 회원 정보 업데이트
            user.setNickname(userProfileDto.getNickname());
            user.setStr_duration(userProfileDto.getStr_duration());
            user.setEnd_duration(userProfileDto.getEnd_duration());
            user.setMessage(userProfileDto.getMessage());
            user.setCompany(userProfileDto.getCompany());

            userRepository.save(user);
        } catch (Exception e) {
            throw new Exception("회원 정보를 업데이트하는 동안 오류가 발생했습니다.", e);
        }
    }

    public void upadateUserPassword(String currentEmail, UserSignUpDto userSignUpDto) throws Exception{
        try {
            User user = getUserByEmail(currentEmail);
            // BCryptPasswordEncoder를 사용하여 새로운 비밀번호 해싱
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(userSignUpDto.getPassword());

            // 회원 정보 업데이트
            user.setPassword(hashedPassword);

            userRepository.save(user);
        } catch (Exception e) {
            throw new Exception("비밀번호 업데이트 하는 동안 오류가 발생했습니다..", e);
        }
    }


    // 메일 내용을 생성하고 임시 비밀번호로 회원 비밀번호를 변경

    public MailDto createMailAndChangePassword(String userEmail) {
        String tempPassword = getTempPassword();
        MailDto mailDTO = new MailDto();
        mailDTO.setAddress(userEmail);
        mailDTO.setTitle("취업어플리케이션 MOS의 임시비밀번호 안내 이메일 입니다.");
        mailDTO.setMessage("안녕하세요. MOS의 임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
                + tempPassword + " 입니다." + "로그인 후에 비밀번호를 변경을 해주세요");
        updatePassword(tempPassword,userEmail);
        return mailDTO;
    }

    //임시 비밀번호로 업데이트
    public void updatePassword(String str, String userEmail){
        Optional<User> existingUser = userRepository.findByEmail(userEmail);
        User user = existingUser.get();
        user.updatePw(str);
    }

    public boolean findPassword(FindPWDto findPWDto) {
        Optional<User> userOptional = userRepository.findByEmail(findPWDto.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getEmail() != null && user.getEmail().equals(findPWDto.getEmail())) {
                return true;
            }
        }
        return false;
    }

    //랜덤함수로 임시비밀번호 구문 만들기
    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    // 메일보내기
    public void mailSend(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());
        message.setFrom("dmsthf1225@naver.com");
        message.setReplyTo("dmsthf1225@naver.com");
        mailSender.send(message);
    }


    public Optional<User> loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return userRepository.findByEmail(userEmail);
    }


    public UserProfileDto getUserProfileByEmail(String email) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // 엔터티 정보를 DTO로 매핑하여 반환
            return new UserProfileDto(
                    user.getNickname(),
                    user.getName(),
                    user.getStr_duration(),
                    user.getEnd_duration(),
                    user.getMessage(),
                    user.getCompany(),
                    user.getTend1(),
                    user.getTend2(),
                    user.getRoomId()
            );
        } else {
            throw new Exception("해당 이메일의 사용자를 찾을 수 없습니다: " + email);
        }
    }
}