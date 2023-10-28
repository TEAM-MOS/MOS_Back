package mos.mosback.login.domain.user.conrtoller;
import mos.mosback.login.domain.user.User;
import mos.mosback.login.domain.user.dto.UserProfileDto;
import mos.mosback.login.domain.user.repository.UserRepository;
import mos.mosback.login.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    // 현재 로그인한 사용자의 정보 가져오기
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    String currentEmail = authentication.getName(); // 현재 사용자의 이메일

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserProfileController(UserService userService, UserRepository userRepository) {

        this.userService = userService;
        this.userRepository = userRepository;

    }

    @PostMapping
    public Map<String, Object> createUserProfile(@RequestBody UserProfileDto userProfileDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 현재 로그인한 사용자의 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentEmail = authentication.getName(); // 현재 사용자의 이메일

            // 회원 정보 생성
            userService.createUser(currentEmail, userProfileDto);

            response.put("status", 200);
            response.put("success", true);
            response.put("message", "회원 정보가 생성되었습니다.");
        } catch (Exception e) {
            e.printStackTrace(); // 이 코드는 예외의 스택 트레이스를 콘솔에 출력합니다.
            response.put("status", 500);
            response.put("success", false);
            response.put("message", "오류가 발생했습니다.");
        }
        return response;
    }


    @PutMapping
    public Map<String, Object> updateUserProfile(@RequestBody UserProfileDto userProfileDto) throws Exception {
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        Map<String, Object> response = new HashMap<>();
        try{
            // 회원 정보 업데이트
            userService.updateUserProfile(currentEmail, userProfileDto);
            response.put("status", 200);
            response.put("success", true);
            response.put("message", "회원 정보가 업데이트되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", 500);
            response.put("success", false);
            response.put("message", "오류가 발생했습니다.");
        }

        return response;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getUserProfile() {
        Map<String, Object> response = new HashMap<>();
        try {
            // 현재 로그인한 사용자의 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentEmail = authentication.getName(); // 현재 사용자의 이메일

            // 현재 사용자의 이메일을 이용하여 프로필 정보를 가져옵니다.
            UserProfileDto userProfileDto = userService.getUserProfileByEmail(currentEmail);

            response.put("status", 200);
            response.put("success", true);
            response.put("data", userProfileDto);

            // 프로필 정보를 클라이언트에 응답으로 반환합니다.
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 에러 발생 시 500 Internal Server Error 반환
            response.put("status", 500);
            response.put("success", false);
            response.put("message", "오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
