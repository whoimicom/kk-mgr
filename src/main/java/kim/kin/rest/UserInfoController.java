package kim.kin.rest;

import kim.kin.kklog.KkLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author choky
 */
@Controller
public class UserInfoController {
    private final Logger logger = LoggerFactory.getLogger(UserInfoController.class);
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private RequestCache requestCache = new HttpSessionRequestCache();


    @RequestMapping("/login-error.html")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }

//    @RequestMapping("/logout")
//    public String logout(Model model) {
//        model.addAttribute("loginError", true);
//        return "index.html";
//    }


    @GetMapping("/login.html")
    @KkLog
    public String login(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String redirectUrl = savedRequest.getRedirectUrl();
            logger.info("redirectUrl：{}", redirectUrl);
        }
        if (Optional.ofNullable(authentication).map(Authentication::getPrincipal).isPresent()) {
            return "/index.html";
        } else {
            return "login.html";
        }

    }

    @GetMapping("/")
    @KkLog
    public void success(HttpServletRequest request, HttpServletResponse response, Authentication authentication, Model model) throws IOException {
        Optional<Object> principalOpt = Optional.ofNullable(authentication.getPrincipal());
        if (principalOpt.isPresent()) {
            Object principal = principalOpt.get();
            logger.info(" / " + principal);
            model.addAttribute("user", principal);
            redirectStrategy.sendRedirect(request, response, "/index.html");
        } else {
            redirectStrategy.sendRedirect(request, response, "/login.html");
        }
    }

    @GetMapping("/index.html")
    @KkLog
    public String index(Authentication authentication, Model model) {
        logger.info("GetMapping /index.html");
        model.addAttribute("user", authentication.getPrincipal());
        return "index.html";
    }

    @GetMapping("/userInfo.html")
    @KkLog
    public String userInfo(Authentication authentication, Model model) {
        logger.info("GetMapping /userInfo.html");
        model.addAttribute("user", authentication.getPrincipal());
        return "user/userInfo.html";
    }

}
