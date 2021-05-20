package kim.kin.config.session;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author choky
 */
@Component
public class KkInvalidSessionStrategy implements InvalidSessionStrategy {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {
        redirectStrategy.sendRedirect(request, response, "/logout");
    }

}