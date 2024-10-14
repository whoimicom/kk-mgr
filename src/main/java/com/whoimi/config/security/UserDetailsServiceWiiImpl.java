package com.whoimi.config.security;

import com.whoimi.model.UserInfo;
import com.whoimi.model.UserKimDetails;
import com.whoimi.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author whoimi
 */
@Service
public class UserDetailsServiceWiiImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceWiiImpl.class);

    @Autowired
    private UserInfoRepository userInfoRepository;


    @Override
    public UserDetails loadUserByUsername(String username) {
        logger.info("Override loadUserByUsername:[{}]", username);
        UserInfo userInfo = userInfoRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        String permissions = "/test,/auth,/index.html";
        UserKimDetails userDetails = new UserKimDetails(
                userInfo.getUsername(), userInfo.getPassword(), true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
        userDetails.setTheme("");
        userDetails.setAvatar(userInfo.getAvatar());
        userDetails.setEmail(userInfo.getEmail());
        userDetails.setMobile(userInfo.getMobile());
        userDetails.setGender(userInfo.getGender());
        userDetails.setId(userInfo.getId());
        userDetails.setPassword(userInfo.getPassword());
        userDetails.setLoginTime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        return userDetails;
    }

}