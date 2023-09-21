package mos.mosback.login.domain.user.conrtoller;

import mos.mosback.login.domain.user.dto.UserProfileDto;
import mos.mosback.login.domain.user.repository.UserRepository;
import mos.mosback.login.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> createUserProfile(@RequestBody UserProfileDto userProfileDto) throws Exception {
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        try{
        // 회원 정보 생성
        userService.createUser(currentEmail, userProfileDto);

        return ResponseEntity.ok("회원 정보가 생성되었습니다.");
        } catch (Exception e){
            e.printStackTrace(); // 이 코드는 예외의 스택 트레이스를 콘솔에 출력합니다.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류가 발생했습니다.");
        }

    }

    @PutMapping
    public ResponseEntity<String> updateUserProfile(@RequestBody UserProfileDto userProfileDto) throws Exception {
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        try{
        // 회원 정보 업데이트
        userService.updateUserProfile(currentEmail, userProfileDto);

        return ResponseEntity.ok("회원 정보가 업데이트되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류가 발생했습니다.");
            }
        }

    // 다른 컨트롤러 메서드...

}

