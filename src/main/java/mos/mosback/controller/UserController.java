package mos.mosback.controller;

import mos.mosback.data.entity.User;
import mos.mosback.repository.UserRepository;
import mos.mosback.service.UserService;
import mos.mosback.web.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("/api")
public class UserController {


    private UserService userService;
    private UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> join(@RequestBody SignUpDTO signUpDTO) {
        if(userService.isEmailDuplicate(signUpDTO.getEmail())) {
            return ResponseEntity.status(400).body("Email already exists");
        }
        userService.join(signUpDTO);
        return ResponseEntity.ok("Join Success!");
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        if(userService.isEmailDuplicate(loginDTO.getEmail())) {
            if(userService.comparePassword(loginDTO.getEmail(), loginDTO.getPassword())) {
                User user = userRepository.findByEmail(loginDTO.getEmail());
                if(user != null) {
                    // 세션에 유저 객체를 저장
                    session.setAttribute("user", user);
                    return ResponseEntity.ok("로그인 성공");
                }
                return ResponseEntity.status(400).body("사용자를 찾을 수 없습니다.");
            }
            return ResponseEntity.status(400).body("비밀번호가 틀렸습니다.");
        }
        return ResponseEntity.status(400).body("사용자를 찾을 수 없습니다.");
    }
    //이메일 중복확인
    @GetMapping("/user-emails/{email}/exists")
    public ResponseEntity<Boolean> isEmailDuplicate(@PathVariable String email){
        return ResponseEntity.ok(userService.isEmailDuplicate(email));
    }

    //비밀번호 변경
    @Transactional
    @PostMapping("/find")
    public ResponseEntity<String> findPw(@RequestBody FindPWDTO findPWDTO){
        if(userService.findPassword(findPWDTO)) {
            MailDTO mailDTO = userService.createMailAndChangePassword(findPWDTO.getEmail());
            userService.mailSend(mailDTO);
            return ResponseEntity.ok("임시 비밀번호 메일 전송, 변경 완료");
        }
        else{
            return ResponseEntity.status(400).body("이메일과 이름이 일치하지 않습니다.");
        }
    }

    @Transactional
    @PostMapping("/send/Email")
    public ResponseEntity<String> sendEmail(@RequestParam("userEmail") String userEmail){
        MailDTO mailDTO = userService.createMailAndChangePassword(userEmail);
        userService.mailSend(mailDTO);

        return ResponseEntity.ok("임시 비밀번호 메일 전송, 변경 완료");
    }

    @GetMapping("/nickname")
    public ResponseEntity<String> findNicknameO() {
        Home_nickResponseDto nicknameDto = userService.getNickname();

        if (nicknameDto != null) {
            return ResponseEntity.ok(nicknameDto.getNickname());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}