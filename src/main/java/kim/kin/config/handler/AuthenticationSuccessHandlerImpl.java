package kim.kin.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import kim.kin.model.KkUserDetails;
import kim.kin.model.UserInfo;
import kim.kin.utils.KkConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author choky
 */
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    private final Logger logger= LoggerFactory.getLogger(AuthenticationSuccessHandlerImpl.class);
    private ObjectMapper mapper = new ObjectMapper();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private SessionRegistry sessionRegistry;

    public AuthenticationSuccessHandlerImpl() {
        super();
    }
    public AuthenticationSuccessHandlerImpl(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String remoteAddress = details.getRemoteAddress();

        Object principal = authentication.getPrincipal();
        KkUserDetails userDetails = (KkUserDetails) principal;
        userDetails.setRemoteAddress(remoteAddress);

//        if (!LoginType.normal.equals(loginType)) {
//            String sessionId = details.getSessionId();
//            sessionRegistry.removeSessionInformation(sessionId);
//            sessionRegistry.registerNewSession(sessionId, authentication.getPrincipal());
//            ConcurrentSessionControlAuthenticationStrategy authenticationStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry);
//            authenticationStrategy.setMaximumSessions(1);
//            authenticationStrategy.onAuthentication(authentication, request, response);
//
//            // 社交账户登录成功后直接 重定向到主页
//            if (LoginType.social.equals(loginType)) {
//                redirectStrategy.sendRedirect(request, response, "/index.html");
//            }
//        }

//        response.setContentType(KkConstant.CONTENT_TYPE_JSON_UTF8);
//        response.getWriter().write(mapper.writeValueAsString(ResponseEntity.ok()));
        redirectStrategy.sendRedirect(request, response, "/index.html");
    }

}