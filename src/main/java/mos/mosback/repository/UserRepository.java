package mos.mosback.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByEmail(String email); // 이메일 통해 회원 조회하기 위함

    boolean existsByEmail(String email);
//    boolean existsByNickname(String nickname);
}