package com.whoimi.rest;

import com.whoimi.config.security.AnonymousWiiAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author whoimi
 */
@RestController
public class DateController {
    private final Logger logger = LoggerFactory.getLogger(DateController.class);


    @RequestMapping("/testRestDate")
    @AnonymousWiiAccess
    public List testRestDate(@RequestBody TimeTemp timeTemp) {
        LocalDate localDate = timeTemp.getLocalDate();
        LocalDateTime localDateTime = timeTemp.getLocalDateTime();
        LocalTime localTime = timeTemp.getLocalTime();
        LocalDateTime localDateTimeN = timeTemp.getLocalDateTimeN();
        logger.info("{}|{}|{}|{}", localDate, localDateTime, localTime,localDateTimeN);
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(localDate);
        objects.add(localDateTime);
        objects.add(localTime);
        objects.add(localDateTimeN);
        objects.add(timeTemp);
        return objects;
    }


}
