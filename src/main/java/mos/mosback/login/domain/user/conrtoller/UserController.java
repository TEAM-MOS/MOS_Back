package mos.mosback.login.domain.user.conrtoller;

import lombok.RequiredArgsConstructor;
import mos.mosback.login.domain.user.User;
import mos.mosback.login.domain.user.dto.*;
import mos.mosback.login.domain.user.repository.UserRepository;
import mos.mosback.login.domain.user.service.UserService;
import mos.mosback.login.global.jwt.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    private Map<Long, User> userMap = new HashMap<>();

    private JwtService jwtService;
   @PostMapping("/sign-up")
public ResponseEntity<Map<String, Object>> signUp(@RequestBody UserSignUpDto userSignUpDto) {
    Map<String, Object> response = new HashMap<>();
    try {
        userService.signUp(userSignUpDto);
        response.put("status", 200);
        response.put("success", true);
        response.put("message", "회원가입 성공");
        return ResponseEntity.ok(response);
    } catch (Exception ex) {
        response.put("status", 500);
        response.put("success", false);
        response.put("message", "회원가입 실패: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}



    @PutMapping("/update/password")
    public ResponseEntity<String> updateUserPassword(@RequestBody UserSignUpDto userSignUpDto) throws Exception {
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        try{
            // 회원 정보 업데이트
            userService.upadateUserPassword(currentEmail, userSignUpDto);

            return ResponseEntity.ok("비밀번호 수정이 완료되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류가 발생했습니다.");
        }
    }


    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }


    @GetMapping("/user-emails/{email}/exists")
    public ResponseEntity<String> checkEmailExists(@PathVariable String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            return ResponseEntity.ok("이메일이 이미 존재합니다.");
        } else {
            return ResponseEntity.ok("사용할 수 있는 이메일입니다.");
        }
    }


    @Transactional
    @PostMapping("/send/email")
    public ResponseEntity<String> sendTemporaryPassword(@RequestBody FindPWDto findPWDto) {
        if (userService.findPassword(findPWDto)) {
            MailDto mailDto = userService.createMailAndChangePassword(findPWDto.getEmail());
            userService.mailSend(mailDto);
            return ResponseEntity.ok("임시 비밀번호 메일 전송 및 변경 완료");
        } else {
            return ResponseEntity.status(400).body("이메일이 존재하지 않거나 이름이 일치하지 않습니다.");
        }
    }
}
