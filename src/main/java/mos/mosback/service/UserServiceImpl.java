package mos.mosback.service;

import mos.mosback.repository.UserInfo;
import mos.mosback.repository.UserRepository;
import mos.mosback.web.dto.UserInfoDto;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository UserRepository;

    public UserServiceImpl(UserRepository UserRepository) {
        this.UserRepository = UserRepository;
    }

    @Override
    public void save(UserInfoDto userInfoDto) {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userInfoDto.getEmail());
        userInfo.setPassword(userInfoDto.getPassword());
        UserRepository.save(userInfo);
    }

    @Override
    public boolean checkEmailDuplicate(String email) {
        return UserRepository.existsByEmail(email);
    }
}
