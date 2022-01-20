package kim.kin.config.security;

import kim.kin.model.UserInfo;
import kim.kin.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: xxm
 * @description:
 * @date: 2021/3/11 16:07
 */
public class WeChatAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (authentication.isAuthenticated()) {
            return authentication;
        }
        //获取过滤器封装的token信息
        WeChatAuthenticationToken authenticationToken = (WeChatAuthenticationToken) authentication;
        String openid = (String) authenticationToken.getPrincipal();
        UserInfo userInfo = userInfoRepository.findByPassword(openid).orElseThrow(() -> new UsernameNotFoundException(openid));

        if (userInfo.getEnabled()) {
            //存放session
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = request.getSession();
            session.setAttribute("username", userInfo.getUsername());
        } else {
            //微信账号没有关联网站账号
            throw new BadCredentialsException("微信授权openid无效，请重新登陆");
        }
//        不通过
        if (userInfo == null) {
            throw new BadCredentialsException("微信授权openid无效，请重新登陆");
        }
        // 根用户拥有全部的权限
        List<String> permissionCodess = null;
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String permissionCode : permissionCodess) {
            if (permissionCode != null && permissionCode != "") {
                GrantedAuthority grantedAuthority =
                        new SimpleGrantedAuthority(permissionCode);
                authorities.add(grantedAuthority);
            }
        }
        WeChatAuthenticationToken authenticationResult = new WeChatAuthenticationToken(openid, authorities);

        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WeChatAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
