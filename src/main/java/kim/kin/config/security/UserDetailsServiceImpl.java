package kim.kin.config.security;

import kim.kin.model.KkSocialUser;
import kim.kin.model.KkUserDetails;
import kim.kin.model.UserInfo;
import kim.kin.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author choky
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService, SocialUserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserInfoRepository userInfoRepository;


    @Override
    public UserDetails loadUserByUsername(String username) {
        logger.info(""+username);
        UserInfo userInfo = null;
        try {
            userInfo = userInfoRepository.findByUsername(username).orElseThrow(Exception::new);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String permissions = "/test,/auth,/index.html";
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