package mos.mosback.service;

import mos.mosback.web.dto.UserInfoDto;

public interface UserService {

    void save(UserInfoDto userInfoDto);

    boolean checkEmailDuplicate(String email);
}
