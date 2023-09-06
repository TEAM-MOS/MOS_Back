package mos.mosback.repository;

import mos.mosback.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

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

}
