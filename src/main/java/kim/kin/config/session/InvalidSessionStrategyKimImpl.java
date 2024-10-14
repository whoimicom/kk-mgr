package kim.kin.config.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.stereotype.Component;


import java.io.IOException;

/**
 * @author whoimi
 */
@Component
public class InvalidSessionStrategyKimImpl implements InvalidSessionStrategy {
    private static final Logger logger = LoggerFactory.getLogger(InvalidSessionStrategyKimImpl.class);
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("InvalidSessionStrategyImpl");
//        redirectStrategy.sendRedirect(request, response, "/logout");
    }

}