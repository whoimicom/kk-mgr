package com.whoimi.service.impl;

import com.whoimi.exception.ServicesWiiException;
import com.whoimi.model.UserInfo;
import com.whoimi.model.UserInfoDTO;
import com.whoimi.repository.UserInfoRepository;
import com.whoimi.service.UserInfoService;
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
        UserInfo userInfo = userInfoRepository.findByUsername(username).orElseThrow(() -> new ServicesWiiException("用户不存在"));
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
