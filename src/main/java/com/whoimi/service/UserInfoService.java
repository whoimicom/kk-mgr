package com.whoimi.service;

import com.whoimi.model.UserInfo;
import com.whoimi.model.UserInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author whoimi
 */
public interface UserInfoService {
    UserInfo save(UserInfoDTO dto);
    UserInfo save(UserInfo dto);

    UserInfo findByUsername(String username);

    Page<UserInfo> findAll(Pageable pageable);
}
