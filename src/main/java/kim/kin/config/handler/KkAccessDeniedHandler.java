package kim.kin.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import kim.kin.utils.KkConstant;
import kim.kin.utils.KkUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author choky
 */
public class KkAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper mapper = new ObjectMapper();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        if (KkUtils.isAjaxRequest(request)) {
            response.setContentType(KkConstant.CONTENT_TYPE_JSON_UTF8);
            response.getWriter().write(this.mapper.writeValueAsString(new ResponseEntity<Object>("AccessDenied", HttpStatus.UNAUTHORIZED)));
        } else {
            redirectStrategy.sendRedirect(request, response, "/access/403");
        }
    }
}