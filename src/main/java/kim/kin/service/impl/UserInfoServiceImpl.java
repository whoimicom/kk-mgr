package kim.kin.service.impl;

import kim.kin.model.UserInfo;
import kim.kin.model.UserInfoDTO;
import kim.kin.repository.UserInfoRepository;
import kim.kin.service.UserInfoService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author choky
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    private final UserInfoRepository userInfoRepository;

    private final PasswordEncoder bcryptEncoder;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository, PasswordEncoder bcryptEncoder) {
        this.userInfoRepository = userInfoRepository;
        this.bcryptEncoder = bcryptEncoder;
    }

    @Override
    public UserInfo save(UserInfoDTO dto) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(dto.getUsername());
        userInfo.setPassword(bcryptEncoder.encode(dto.getPassword()));
        return userInfoRepository.save(userInfo);
    }
}
