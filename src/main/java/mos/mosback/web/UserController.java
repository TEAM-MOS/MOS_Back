package mos.mosback.web;

import mos.mosback.service.UserService;
import mos.mosback.web.dto.UserInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserInfoDto userInfoDto) {
        if (userService.checkEmailDuplicate(userInfoDto.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        userService.save(userInfoDto);
        return ResponseEntity.ok("Signup successful");
    }

    @GetMapping("/user-emails/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email){
        return ResponseEntity.ok(userService.checkEmailDuplicate(email));
    }
}
