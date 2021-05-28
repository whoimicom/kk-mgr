package kim.kin.rest;

import kim.kin.kklog.KkLog;
import kim.kin.model.UserInfo;
import kim.kin.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private UserInfoService userInfoService;


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
            return "redirect:/index.html";
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
    public String userInfo(Model model, Pageable pageable) {
        logger.info("GetMapping /userInfo.html");
        Page<UserInfo> page = userInfoService.findAll(pageable);
        model.addAttribute("page", page);
        return "user/userInfo.html";
    }

    @GetMapping("/testFragment.html")
    @KkLog
    public String testFragment(Model model, Pageable pageable) {
        logger.info("GetMapping /testFragment.html");
        Page<UserInfo> page = userInfoService.findAll(pageable);
        model.addAttribute("page", page);
        return "user/testFragment.html";
    }

}
