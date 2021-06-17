package kim.kin.rest;

import kim.kin.config.security.AnonymousKimAccess;
import kim.kin.kklog.LogKimAnnotation;
import kim.kin.model.UserInfo;
import kim.kin.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author choky
 */
@RestController
public class DateController {
    private final Logger logger = LoggerFactory.getLogger(DateController.class);


    @RequestMapping("/testRestDate")
    @AnonymousKimAccess
    public List testRestDate(@RequestBody TimeTemp timeTemp) {
        LocalDate localDate = timeTemp.getLocalDate();
        LocalDateTime localDateTime = timeTemp.getLocalDateTime();
        LocalTime localTime = timeTemp.getLocalTime();
        logger.info("{}|{}|{}|", localDate, localDateTime, localTime);
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(localDate);
        objects.add(localDateTime);
        objects.add(localTime);
        return objects;
    }


}
