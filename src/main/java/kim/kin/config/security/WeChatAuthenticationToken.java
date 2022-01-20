package kim.kin.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author: xxm
 * @description:微信验证的token
 * @date: 2021/3/11 14:04
 */
public class WeChatAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = -6231962326068951783L;


    public WeChatAuthenticationToken(Object principal) {
        super(principal, "");
    }


    public WeChatAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(principal, "", authorities);
    }

}
