package kim.kin.config.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author: xxm
 * @description:微信openid登陆验证的过滤器
 * @date: 2021/3/11 14:58
 */
public class WeChatAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    private String openidParameter = "openid";



    public WeChatAuthenticationFilter() {
        super("/wechat/weChatLogin");
        //super.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        if (!request.getMethod().equals(HttpMethod.GET.name())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String openid = obtainOpenid(request);
        if (openid == null || openid.length() == 0) {
            throw new BadCredentialsException("uid or openid is null.");
        }

        WeChatAuthenticationToken authRequest = new WeChatAuthenticationToken(openid);

        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

        return this.getAuthenticationManager().authenticate(authRequest);

    }

    protected String obtainOpenid(HttpServletRequest request) {
        String openid = request.getParameter(this.openidParameter);
        return openid == null ? "" : openid.trim();
    }

}
