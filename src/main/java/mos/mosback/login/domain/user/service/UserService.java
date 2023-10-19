package mos.mosback.login.domain.user.service;

import mos.mosback.login.domain.user.Role;
import mos.mosback.login.domain.user.User;
import mos.mosback.login.domain.user.dto.FindPWDto;
import mos.mosback.login.domain.user.dto.MailDto;

import mos.mosback.login.domain.user.dto.UserProfileDto;
import mos.mosback.login.domain.user.dto.UserSignUpDto;
import mos.mosback.login.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    // 사용자의 이미지 URL 업데이트 메서드
    public void uploadAndSaveImage(String currentEmail, MultipartFile file) {
        // 이미지를 업로드하고 저장하는 로직을 구현합니다.
        String imageUrl = saveImageToFileSystem(file);

        // imageUrl을 데이터베이스에 저장하도록 업데이트합니다.
        updateImageUrl(currentEmail, imageUrl);
    }

    // 사용자의 이미지 URL 업데이트 메서드
    public void updateImageUrl(String userEmail, String imageUrl) {
        // 이메일을 사용하여 사용자 정보를 데이터베이스에서 조회합니다.
        User user = userRepository.findByEmail(userEmail).get();

        if (user != null) {
            // 사용자 정보가 존재하면 이미지 URL을 업데이트합니다.
            user.setImageUrl(imageUrl);
            userRepository.save(user); // 변경된 정보를 저장합니다.
        } else {
            throw new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다: " + userEmail);
        }
    }

    // 이미지를 파일 시스템에 저장하는 로직을 구현합니다.
    private String saveImageToFileSystem(MultipartFile file) {
        // 이미지를 저장하고 저장된 경로를 반환하는 로직을 작성합니다.
        // 실제로는 파일 시스템에 이미지를 저장하고 해당 이미지의 경로를 반환해야 합니다.
        // 이 예제에서는 경로 대신 "image_url_placeholder"를 반환합니다.
        return "image_url_placeholder";
    }


}


