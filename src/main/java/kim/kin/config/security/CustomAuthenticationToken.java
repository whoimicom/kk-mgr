package kim.kin.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author: xxm
 * @description:用户名和密码验证的token
 * @date: 2021/3/10 14:57
 */
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
    /**
     *
     */
    private static final long serialVersionUID = -1076492615339314113L;

    public CustomAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }
    public CustomAuthenticationToken(Object principal, Object credentials,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
