package kim.kin.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import kim.kin.utils.KkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author choky
 */
@Component
public class AccessDeniedKimImpl implements AccessDeniedHandler {
    private static final Logger logger = LoggerFactory.getLogger(AccessDeniedKimImpl.class);
    private ObjectMapper mapper = new ObjectMapper();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        logger.info("");
        if (KkUtils.isAjaxRequest(request)) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(this.mapper.writeValueAsString(new ResponseEntity<Object>("AccessDenied", HttpStatus.UNAUTHORIZED)));
        } else {
            redirectStrategy.sendRedirect(request, response, "/access/403");
        }
    }
}