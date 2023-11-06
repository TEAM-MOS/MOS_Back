package mos.mosback.login.domain.user.controller;

import mos.mosback.login.domain.user.User;
import mos.mosback.login.domain.user.dto.UserProfileDto;
import mos.mosback.login.domain.user.repository.UserRepository;
import mos.mosback.login.domain.user.service.FileService;
import mos.mosback.login.domain.user.service.UserService;
import mos.mosback.stRoom.dto.StudyMembershipStatusResponseDto;
import mos.mosback.stRoom.repository.StRoomRepository;
import mos.mosback.stRoom.repository.StudyMemberRepository;
import mos.mosback.stRoom.service.StRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/profile")
public class UserProfileController {
//    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    // 현재 로그인한 사용자의 정보 가져오기
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    String currentEmail = authentication.getName(); // 현재 사용자의 이메일

    private final UserService userService;
    private final UserRepository userRepository;

    private final StRoomService stRoomService;
    private final StudyMemberRepository studyMemberRepository;
    private final StRoomRepository stRoomRepository;

    private final FileService fileService;

    @Autowired
    public UserProfileController(UserService userService, UserRepository userRepository, StRoomService stRoomService, StudyMemberRepository studyMemberRepository, StRoomRepository stRoomRepository, FileService fileService) {

        this.userService = userService;
        this.userRepository = userRepository;
        this.stRoomService = stRoomService;
        this.studyMemberRepository = studyMemberRepository;
        this.stRoomRepository = stRoomRepository;
        this.fileService = fileService;
    }

    @PostMapping
    public Map<String, Object> createUserProfile(@RequestPart(value = "file") MultipartFile file,
                                                 @RequestPart(value = "userProfileDto") UserProfileDto userProfileDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 현재 로그인한 사용자의 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentEmail = authentication.getName(); // 현재 사용자의 이메일

            // 회원 정보 생성
            userService.createUser(currentEmail, userProfileDto);
            // 이미지 업로드 및 URL 저장
            String imageUrl = fileService.uploadFile(file, userProfileDto.getId());
            userProfileDto.setImageUrl(imageUrl);

            // 사용자 정보에 이미지 URL 업데이트
            userService.updateUserProfileImageUrl(userProfileDto.getId(), imageUrl);

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
    public Map<String, Object> updateUserProfile(UserProfileDto userProfileDto) throws Exception {
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        Map<String, Object> response = new HashMap<>();
        try {
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

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStudyMembershipStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // 현재 사용자의 이메일

        Map<String, Object> response = new HashMap<>();
        HttpStatus httpStatus;
        boolean success = true;
        List<StudyMembershipStatusResponseDto> membershipStatusList;

        try {
            // userService에서 스터디 멤버십 상태 정보를 가져옵니다.
            membershipStatusList = userService.getStudyMembershipStatus(userEmail);
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            membershipStatusList = Collections.emptyList();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            success = false;
            response.put("error", e.getMessage());
        }

        response.put("status", httpStatus.value());
        response.put("success", success);
        response.put("data", membershipStatusList);

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadUserImage(@PathVariable Long id,
                                             @RequestParam("file") MultipartFile file,
                                             @ModelAttribute UserProfileDto userProfileDto) {
        try {
            userService.uploadAndSaveImage(id, file);
            return new ResponseEntity<>("User image uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error occurred while uploading image", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<?> getUserImage(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            String imageUrl = user.getImageUrl();
            return new ResponseEntity<>(imageUrl, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }


}

