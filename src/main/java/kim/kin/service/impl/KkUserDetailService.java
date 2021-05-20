package kim.kin.service.impl;

import kim.kin.model.KkSocialUser;
import kim.kin.model.KkUserDetails;
import kim.kin.model.UserInfo;
import kim.kin.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class KkUserDetailService implements UserDetailsService, SocialUserDetailsService {

    @Autowired
    private UserInfoRepository userInfoRepository;


    @Override
    public UserDetails loadUserByUsername(String username) {
        UserInfo userInfo = null;
        try {
            userInfo = userInfoRepository.findByUsername(username).orElseThrow(Exception::new);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String permissions = "/test,/auth";
        KkUserDetails userDetails = new KkUserDetails(
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

    @Override
    public SocialUserDetails loadUserByUserId(String username) {
        UserInfo userInfo = null;
        try {
            userInfo = userInfoRepository.findByUsername(username).orElseThrow(Exception::new);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String permissions = "/test,/auth";
        KkSocialUser userDetails = new KkSocialUser(
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