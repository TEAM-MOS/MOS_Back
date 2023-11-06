package mos.mosback.login.domain.user.service;

import javassist.NotFoundException;
import mos.mosback.login.domain.user.Role;
import mos.mosback.login.domain.user.User;
import mos.mosback.login.domain.user.dto.FindPWDto;
import mos.mosback.login.domain.user.dto.MailDto;

import mos.mosback.login.domain.user.dto.UserProfileDto;
import mos.mosback.login.domain.user.dto.UserSignUpDto;
import mos.mosback.login.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import mos.mosback.stRoom.domain.stRoom.StRoomEntity;
import mos.mosback.stRoom.domain.stRoom.StudyMemberEntity;
import mos.mosback.stRoom.dto.StudyMembershipStatusResponseDto;
import mos.mosback.stRoom.repository.StRoomRepository;
import mos.mosback.stRoom.repository.StudyMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.amazonaws.services.ec2.model.Scope.Region;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private StudyMemberRepository studyMemberRepository;

    private StRoomRepository stRoomRepository;

    @Autowired
    private final FileService fileService;

    public User getUserById(Long id) throws Exception {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new Exception("현재 로그인한 사용자를 찾을 수 없습니다.");
        }
    }

    public void signUp(UserSignUpDto userSignUpDto) throws Exception {

        if (userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .email(userSignUpDto.getEmail())
                .password(userSignUpDto.getPassword())
                .role(Role.GUEST)
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
    }



    public User getUserByEmail(String email) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new Exception("해당 이메일의 사용자를 찾을 수 없습니다: " + email);
        }
    }


    //마이페이지정보입력
    public void createUser(String currentEmail, UserProfileDto userProfileDto) throws Exception {
        if (userRepository.findByNickname(userProfileDto.getNickname()).isPresent()) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        try {
            User user = getUserByEmail(currentEmail);

            // 회원 정보 생성
            user.setNickname(userProfileDto.getNickname());
            user.setName(userProfileDto.getName());
            user.setStr_duration(userProfileDto.getStr_duration());
            user.setEnd_duration(userProfileDto.getEnd_duration());
            user.setMessage(userProfileDto.getMessage());
            user.setCompany(userProfileDto.getCompany());
            user.setTend1(userProfileDto.getTend1());
            user.setTend2(userProfileDto.getTend2());
            user.setRole(Role.USER);

            userRepository.save(user);
        } catch (Exception e) {
            throw new Exception("회원 정보를 생성하는 동안 오류가 발생했습니다.", e);
        }
    }

    public void updateUserProfileImageUrl(Long userId, String imageUrl) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setImageUrl(imageUrl);
            userRepository.save(user);
        } else {
            throw new Exception("해당 ID의 사용자를 찾을 수 없습니다: " + userId);
        }
    }


    //마이페이지 업데이트
    public void updateUserProfile(String currentEmail, UserProfileDto userProfileDto ) throws Exception {
        try {
            User user = getUserByEmail(currentEmail);

            // 회원 정보 업데이트
            user.setNickname(userProfileDto.getNickname());
            user.setName(userProfileDto.getName());
            user.setStr_duration(userProfileDto.getStr_duration());
            user.setEnd_duration(userProfileDto.getEnd_duration());
            user.setMessage(userProfileDto.getMessage());
            user.setCompany(userProfileDto.getCompany());
            user.setTend1(userProfileDto.getTend1());
            user.setTend2(userProfileDto.getTend2());
            user.setRoomId(userProfileDto.getRoomId());




            userRepository.save(user);
        } catch (Exception e) {
            throw new Exception("회원 정보를 업데이트하는 동안 오류가 발생했습니다.", e);
        }
    }

    public void upadateUserPassword(String currentEmail, UserSignUpDto userSignUpDto) throws Exception{
        try {
            User user = getUserByEmail(currentEmail);
            // BCryptPasswordEncoder를 사용하여 새로운 비밀번호 해싱
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(userSignUpDto.getPassword());

            // 회원 정보 업데이트
            user.setPassword(hashedPassword);

            userRepository.save(user);
        } catch (Exception e) {
            throw new Exception("비밀번호 업데이트 하는 동안 오류가 발생했습니다..", e);
        }
    }


// 메일 내용을 생성하고 임시 비밀번호로 회원 비밀번호를 변경
    public MailDto createMailAndChangePassword(String userEmail) {
        String tempPassword = getTempPassword();
        MailDto mailDTO = new MailDto();
        mailDTO.setAddress(userEmail);
        mailDTO.setTitle("취업어플리케이션 MOS의 임시비밀번호 안내 이메일 입니다.");
        mailDTO.setMessage("안녕하세요. MOS의 임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
                + tempPassword + " 입니다." + "로그인 후에 비밀번호를 변경을 해주세요");
        updatePassword(tempPassword,userEmail);
        return mailDTO;
    }

    //임시 비밀번호로 업데이트
    public void updatePassword(String str, String userEmail){
        Optional<User> existingUser = userRepository.findByEmail(userEmail);
        User user = existingUser.get();
        user.updatePw(str);
    }

    public boolean findPassword(FindPWDto findPWDto) {
        Optional<User> userOptional = userRepository.findByEmail(findPWDto.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getEmail() != null && user.getEmail().equals(findPWDto.getEmail())) {
                return true;
            }
        }
        return false;
    }

    //랜덤함수로 임시비밀번호 구문 만들기
    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    // 메일보내기
    public void mailSend(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());
        message.setFrom("dmsthf1225@naver.com");
        message.setReplyTo("dmsthf1225@naver.com");
        mailSender.send(message);
    }


    public Optional<User> loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return userRepository.findByEmail(userEmail);
    }

    // 사용자의 이미지 URL 업데이트 메서드
    public void uploadAndSaveImage(Long id, MultipartFile file) throws IOException {
        // id를 사용하여 사용자 정보를 데이터베이스에서 조회합니다.
        User user = userRepository.findById(id).get();

        if (user != null) {
            // 이미지를 S3에 업로드하고 URL을 받아옵니다.
            String imageUrl = fileService.uploadFile(file, user.getId());

            // 사용자 정보가 존재하면 이미지 URL을 업데이트합니다.
            user.setImageUrl(imageUrl);
            userRepository.save(user); // 변경된 정보를 저장합니다.
        } else {
            throw new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다: " + id);
        }
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserProfileDto getUserProfileByEmail(String email) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // 엔터티 정보를 DTO로 매핑하여 반환
            return new UserProfileDto(
                    user.getId(),
                    user.getNickname(),
                    user.getName(),
                    user.getStr_duration(),
                    user.getEnd_duration(),
                    user.getMessage(),
                    user.getCompany(),
                    user.getTend1(),
                    user.getTend2(),
                    user.getRoomId(),
                    user.getImageUrl()
            );
        } else {
            throw new Exception("해당 이메일의 사용자를 찾을 수 없습니다: " + email);
        }
    }


    public List<StRoomEntity> getStudyGroupsForUserByEmail(String email) throws NotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getStRooms();
        } else {
            throw new NotFoundException("사용자를 찾을 수 없습니다.");
        }
    }



    public List<StRoomEntity> getStudyGroupsForUserByMemberId(Long memberId) {
        // 사용자의 memberId를 이용해 사용자가 참여한 스터디 그룹 목록을 조회합니다.
        List<StudyMemberEntity> userStudyMemberships = studyMemberRepository.findAllByMemberId(memberId);

        // 사용자가 참여한 스터디 그룹 목록을 담을 리스트를 생성합니다.
        List<StRoomEntity> userStudyGroups = stRoomRepository.findByMembersIn(userStudyMemberships);

        return userStudyGroups;
    }

//    public List<StudyMembershipStatusResponseDto> getStudyMembershipStatus(String userEmail) throws NotFoundException {
//        User user = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
//
//        Long memberId = user.getId();
//        List<StudyMemberEntity> userStudyMemberships = studyMemberRepository.findAllByMemberId(memberId);
//
//        Map<StRoomEntity, StudyMemberEntity> latestStudyMembershipsMap = userStudyMemberships.stream()
//                .collect(Collectors.toMap(
//                        StudyMemberEntity::getStRoom,
//                        Function.identity(),
//                        (m1, m2) -> m1.getJoinedAt().isAfter(m2.getJoinedAt()) ? m1 : m2
//                ));
//
//        List<StudyMembershipStatusResponseDto> membershipStatusList = latestStudyMembershipsMap.entrySet().stream()
//                .map(entry -> {
//                    StRoomEntity stRoom = entry.getKey();
//                    StudyMemberEntity studyMembership = entry.getValue();
//                    String status = studyMembership.getStatus().name();
//                    return new StudyMembershipStatusResponseDto(stRoom.getTitle(), status);
//                })
//                .collect(Collectors.toList());
//
//        return membershipStatusList;
//    }

//        내가 가입한 스터디의 상태를 보여줌(하나만 보여주는 문제)
    public List<StudyMembershipStatusResponseDto> getStudyMembershipStatus(String userEmail) throws NotFoundException {
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

    Long memberId = user.getId();
   List<StudyMemberEntity> userStudyMemberships = studyMemberRepository.findAllByMemberId(memberId);

    List<StudyMembershipStatusResponseDto> membershipStatusList = userStudyMemberships.stream()
            .map(member -> {
                StRoomEntity stRoom = member.getStRoom();
                String status = member.getStatus().name();
                return new StudyMembershipStatusResponseDto(stRoom.getTitle(), status);
            })
            .collect(Collectors.toList());

    return membershipStatusList;
    }


//    public List<StudyMembershipStatusResponseDto> getStudyMembershipStatus(String userEmail) throws NotFoundException {
//        User user = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
//
//        Long memberId = user.getId();
//         List<StudyMemberEntity> userStudyMemberships = studyMemberRepository.findAllByMemberId(memberId);
//
//        List<StudyMembershipStatusResponseDto> membershipStatusList = userStudyMemberships.stream()
//                .map(member -> {
//                    StRoomEntity stRoom = member.getStRoom();
//                    String status = member.getStatus().name();
//                    return new StudyMembershipStatusResponseDto(stRoom.getTitle(), status);
//                })
//                .collect(Collectors.toList());
//
//        return membershipStatusList;
//    }





}


