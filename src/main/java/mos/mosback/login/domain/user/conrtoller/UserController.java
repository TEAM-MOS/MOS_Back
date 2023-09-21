package mos.mosback.login.domain.user.conrtoller;

import lombok.RequiredArgsConstructor;
import mos.mosback.login.domain.user.User;
import mos.mosback.login.domain.user.dto.FindPWDto;
import mos.mosback.login.domain.user.dto.MailDto;
import mos.mosback.login.domain.user.dto.UserInfo;
import mos.mosback.login.domain.user.dto.UserSignUpDto;
import mos.mosback.login.domain.user.repository.UserRepository;
import mos.mosback.login.domain.user.service.UserService;
import mos.mosback.login.global.jwt.service.JwtService;
import org.springframework.http.ResponseEntity;
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
    public String signUp(@RequestBody UserSignUpDto userSignUpDto) throws Exception {
        userService.signUp(userSignUpDto);
        return "회원가입 성공";
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
