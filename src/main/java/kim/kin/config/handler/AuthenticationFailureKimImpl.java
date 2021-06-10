package kim.kin.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import kim.kin.utils.KkConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author choky
 */
@Component
public class AuthenticationFailureKimImpl implements AuthenticationFailureHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String authMsg = exception.getMessage();
        if (exception instanceof UsernameNotFoundException) {
            authMsg = "用户不存在！";
        } else if (exception instanceof BadCredentialsException) {
            authMsg = "用户名或密码错误！";
        } else if (exception instanceof LockedException) {
            authMsg = "用户已被锁定！";
        } else if (exception instanceof DisabledException) {
            authMsg = "用户不可用！";
        } else if (exception instanceof AccountExpiredException) {
            authMsg = "账户已过期！";
        } else if (exception instanceof CredentialsExpiredException) {
            authMsg = "用户密码已过期！";
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authMsg)));
    }
}