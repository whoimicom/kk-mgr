package kim.kin.config.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author choky
 */
public class LogoutHandlerKimImpl implements LogoutHandler {
private static final Logger logger = LoggerFactory.getLogger(LogoutHandlerKimImpl.class);
    private SessionRegistry sessionRegistry;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String sessionId = request.getRequestedSessionId();
        logger.info(" sessionRegistry.removeSessionInformation(); sessionId:"+sessionId);
        if (sessionId != null) {
            sessionRegistry.removeSessionInformation(sessionId);
        }
    }

    public SessionRegistry getSessionRegistry() {
        return sessionRegistry;
    }
    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }
}