package mos.mosback.login.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import mos.mosback.login.domain.user.User;
import mos.mosback.login.domain.user.dto.*;
import mos.mosback.login.domain.user.repository.UserRepository;
import mos.mosback.login.domain.user.service.UserService;
import mos.mosback.login.global.jwt.service.JwtService;
import mos.mosback.stRoom.domain.stRoom.StRoomEntity;
import mos.mosback.stRoom.dto.StRoomResponseDto;
import mos.mosback.stRoom.service.StRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    private final StRoomService stRoomService;

    private Map<Long, User> userMap = new HashMap<>();

    private final JwtService jwtService;

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
    public Map<String, Object> updateUserPassword(@RequestBody UserSignUpDto userSignUpDto) throws Exception {
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        Map<String, Object> response = new HashMap<>();
        try{
            // 회원 정보 업데이트
            userService.upadateUserPassword(currentEmail, userSignUpDto);
            response.put("status", 200);
            response.put("success", true);
            response.put("message", "비밀번호 수정이 완료되었습니다.");
        } catch (Exception e) {
            response.put("status", 500);
            response.put("success", false);
            response.put("message", "비밀번호 수정 실패: " + e.getMessage());
        }
            return response;
    }


    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }


    @GetMapping("/user-emails/{email}/exists")
    public Map<String, Object> checkEmailExists(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            response.put("status", 500);
            response.put("success", true);
            response.put("message", "이메일이 이미 존재합니다.");
        } else {
            response.put("status", 200);
            response.put("success", true);
            response.put("message", "사용할 수 있는 이메일입니다.");
        }
        return response;
    }



    @Transactional
    @PostMapping("/send/email")
    public ResponseEntity<Map<String, Object>> sendTemporaryPassword(@RequestBody FindPWDto findPWDto) {
        Map<String, Object> response = new HashMap<>();
        if (userService.findPassword(findPWDto)) {
            MailDto mailDto = userService.createMailAndChangePassword(findPWDto.getEmail());
            userService.mailSend(mailDto);
            response.put("status", 200);
            response.put("success", true);
            response.put("message", "임시 비밀번호 메일 전송 및 변경 완료");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", 400);
            response.put("success", false);
            response.put("message", "이메일이 존재하지 않거나 이름이 일치하지 않습니다.");
            return ResponseEntity.status(400).body(response);
        }
    }


    @GetMapping("/user/list")
    public ResponseEntity<Map<String, Object>> getLeaderStudies() {
        Map<String, Object> response = new HashMap<>();
        try {
            // 현재 로그인한 사용자의 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName(); // 현재 사용자의 이메일

            List<StRoomResponseDto> leaderStudies = stRoomService.getLeaderStudies(userEmail);

            response.put("status", HttpStatus.OK.value());
            response.put("success", true);
            response.put("data", leaderStudies);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("success", false);
            response.put("error", "Internal Server Error: " + e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
