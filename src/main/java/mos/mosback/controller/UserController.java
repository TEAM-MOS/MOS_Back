package mos.mosback.controller;

import com.zaxxer.hikari.pool.HikariProxyCallableStatement;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import mos.mosback.data.entity.User;
import mos.mosback.repository.UserRepository;
import mos.mosback.service.UserService;
import mos.mosback.web.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import mos.mosback.Provider.JwtTokenProvider;

import javax.transaction.Transactional;

@Controller
@RequestMapping("/api")
public class UserController {

    private UserService userService;
    private UserRepository userRepository;
    private String secretKey ="mosback";

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    //회원가입 이메일,닉네임,기간 마이페이지에 있는 erd
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
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        if(userService.isEmailDuplicate(loginDTO.getEmail())) {
            if(userService.comparePassword(loginDTO.getEmail(),loginDTO.getPassword())) {
                User user = userRepository.findByEmail(loginDTO.getEmail());
                if(user != null) {
                    return ResponseEntity.ok(JwtTokenProvider.createToken(user.getEmail(), user.getPassword()));
                }
                return ResponseEntity.status(400).body("사용자를 찾을 수 없습니다.");
            }
            return ResponseEntity.status(400).body("비밀번호가 틀렸습니다.");
        }
        return ResponseEntity.status(400).body("사용자를 찾을 수 없습니다.");
    }

    //회원 정보 수정
    @PutMapping("/update")
    public User updateInfo(@RequestBody UserUpdateDTO userUpdateDTO, @RequestHeader("Authorization") String jwtToken) {
        Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(jwtToken).getBody();
        User user = userRepository.findByEmail(claims.getSubject());
        userService.updateUserInfo(user, userUpdateDTO);
        return user;
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
            return ResponseEntity.status(400).body("해당 사용자가 없습니다.");
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
