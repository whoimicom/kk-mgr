package kim.kin.service.impl;

import kim.kin.exception.ServicesKimException;
import kim.kin.model.UserInfo;
import kim.kin.model.UserInfoDTO;
import kim.kin.repository.UserInfoRepository;
import kim.kin.service.UserInfoService;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author whoimi
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

    @Override
    public UserInfo save(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);

    }

    @Override
    public UserInfo findByUsername(String username) {
        UserInfo userInfo = userInfoRepository.findByUsername(username).orElseThrow(() -> new ServicesKimException("用户不存在"));
        return userInfo;
    }

    @Override
    public Page<UserInfo> findAll(Pageable pageable) {
        UserInfo userInfo = new UserInfo();
        userInfo.setEnabled(Boolean.TRUE);
        userInfo.setUsername("kinkim");
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
//        return userInfoRepository.findAll(Example.of(userInfo, matcher), pageable);
        return userInfoRepository.findAll(pageable);
    }
}
