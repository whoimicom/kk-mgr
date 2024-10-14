package kim.kin.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kim.kin.model.UserKimDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author whoimi
 */
@Component
public class AuthenticationSuccessKimImpl implements AuthenticationSuccessHandler {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessKimImpl.class);
    private ObjectMapper mapper = new ObjectMapper();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private RequestCache requestCache = new HttpSessionRequestCache();
    private SessionRegistry sessionRegistry;

    public AuthenticationSuccessKimImpl() {
        super();
    }

    public AuthenticationSuccessKimImpl(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String remoteAddress = details.getRemoteAddress();

        Object principal = authentication.getPrincipal();
        UserKimDetails userDetails = (UserKimDetails) principal;
        userDetails.setRemoteAddress(remoteAddress);
        request.getSession().setAttribute("userDetails", userDetails);

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

//        SavedRequest savedRequest = requestCache.getRequest(request, response);
//        String redirectUrl = savedRequest.getRedirectUrl();
//        redirectStrategy.sendRedirect(request, response, redirectUrl);

        redirectStrategy.sendRedirect(request, response, "/index.html");
    }
}