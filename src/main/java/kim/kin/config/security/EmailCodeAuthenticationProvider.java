package kim.kin.config.security;

import kim.kin.model.UserInfo;
import kim.kin.model.UserKimDetails;
import kim.kin.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: crush
 * @Date: 2021-09-08 21:14
 * version 1.0
 */

public class EmailCodeAuthenticationProvider implements AuthenticationProvider {
private static final Logger log = LoggerFactory.getLogger(EmailCodeAuthenticationProvider.class);
    private UserInfoRepository userInfoRepository;

    public EmailCodeAuthenticationProvider(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }


    /**
     * 认证
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        log.info("EmailCodeAuthentication authentication request: %s", authentication);
        EmailCodeAuthenticationToken token = (EmailCodeAuthenticationToken) authentication;
        String email = (String) token.getPrincipal();
        Object credentials = token.getCredentials();
        System.out.println(credentials);
        UserInfo userInfo = userInfoRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        System.out.println(token.getPrincipal());
        if (userInfo == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        //通过用户获取权限
        List<String> permissionCodess = new ArrayList<>(1);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String permissionCode : permissionCodess) {
            if (permissionCode != null && permissionCode != "") {
                GrantedAuthority grantedAuthority =
                        new SimpleGrantedAuthority(permissionCode);
                authorities.add(grantedAuthority);
            }
        }

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

        EmailCodeAuthenticationToken result = new EmailCodeAuthenticationToken(userDetails, authorities);
//        EmailCodeAuthenticationToken result = new EmailCodeAuthenticationToken(user, user.getAuthorities());
                /*
                Details 中包含了 ip地址、 sessionId 等等属性 也可以存储一些自己想要放进去的内容
                */
        result.setDetails(token.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return EmailCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
