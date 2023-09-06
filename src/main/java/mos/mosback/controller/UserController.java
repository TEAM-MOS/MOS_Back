package mos.mosback.controller;

import mos.mosback.repository.UserRepository;
import mos.mosback.service.UserService;
import mos.mosback.web.dto.FindPWDTO;
import mos.mosback.web.dto.MailDTO;
import mos.mosback.web.dto.SignUpDTO;
import mos.mosback.web.dto.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@Controller
@RequestMapping("/api")
public class UserController {


    private UserService userService;


    private UserRepository userRepository;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> join(@RequestBody SignUpDTO signUpDTO) {
        if(userService.isEmailDuplicate(signUpDTO.getEmail())) {
            return ResponseEntity.status(400).body("Email already exists");
        }
        userService.join(signUpDTO);
        return ResponseEntity.ok("Join Success!");
    }


    @GetMapping("/user-emails/{email}/exists")
    public ResponseEntity<Boolean> isEmailDuplicate(@PathVariable String email){
        return ResponseEntity.ok(userService.isEmailDuplicate(email));
    }


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


}