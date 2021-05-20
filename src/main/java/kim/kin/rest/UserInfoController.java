package kim.kin.rest;

import kim.kin.kklog.KkLog;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author choky
 */
@Controller
public class UserInfoController {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @RequestMapping({"/hello"})
    public String firstPage() {
        return "Hello World";
    }

    @RequestMapping("/login.html")
    @KkLog
    public String login() {
        return "login.html";
    }

    @RequestMapping("/login-error.html")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }

//    @GetMapping("/login")
//    public String login(HttpServletRequest request, HttpServletResponse response) {
////        SavedRequest savedRequest = requestCache.getRequest(request, response);
////        if (savedRequest != null) {
////            String redirectUrl = savedRequest.getRedirectUrl();
////            log.info("引发跳转的请求是：{}", redirectUrl);
////        }
//        return "login";
//    }

    @GetMapping("/")
    public void success(HttpServletRequest request, HttpServletResponse response) throws IOException {
        redirectStrategy.sendRedirect(request, response, "/login.html");
    }

    //    @GetMapping("index")
//    public String index(Authentication authentication, Model model) {
//        model.addAttribute("user", authentication.getPrincipal());
//        return "index";
//    }

    @RequestMapping("index.html")
    public String index() {
        return "/layout/index.html";
    }

}
