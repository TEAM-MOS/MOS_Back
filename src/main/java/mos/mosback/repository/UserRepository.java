package mos.mosback.repository;

import mos.mosback.data.entity.User;
import mos.mosback.web.dto.Home_nickResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<UserInfo> findByEmail(String email); // 이메일 통해 회원 조회하기 위함



//    boolean existsByEmail(String email);

//    void saveUser(User user);

//    UserInfo findUserByUserId(String userEmail);

//    void updateUserPassword(Long id, String pw);
//    boolean existsByNickname(String nickname);

    User findByEmail(String email);
    Optional<User> findById(Long id);


    @Query("SELECT new mos.mosback.web.dto.Home_nickResponseDto (u) FROM User u")
    Home_nickResponseDto findNickname();
}
