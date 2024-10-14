package kim.kin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.springframework.http.ResponseEntity.badRequest;

/**
 * @author whoimi
 */
@RestControllerAdvice
public class CommExceptionKimHandler {
    private final Logger logger = LoggerFactory.getLogger(CommExceptionKimHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> response(Exception e) {
        logger.error("RestControllerAdvice：", e);
        HttpHeaders resHeader = new HttpHeaders();
        resHeader.add("resCode", "500");
        return badRequest().headers(resHeader).body(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<Object> authenticationException(AuthenticationException e) {
        logger.error("RestControllerAdvice：", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    /**
     * get param trans
     * @param binder WebDataBinder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                logger.debug("initBinder LocalDate[{}]",text);
                setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                logger.debug("initBinder LocalDateTime[{}]",text);
                setValue(LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        });
        binder.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                logger.debug("initBinder LocalTime[{}]",text);
                setValue(LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
        });
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                logger.debug("initBinder Date[{}]",text);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    setValue(formatter.parse(text));
                } catch (Exception e) {
                    throw new RuntimeException(String.format("Error parsing %s to Date", text));
                }
            }
        });
    }
}
