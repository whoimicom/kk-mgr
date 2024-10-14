package kim.kin.config.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author whoimi
 */
@Component
public class SessionInformationExpiredKimImpl implements SessionInformationExpiredStrategy {
    private static final Logger logger = LoggerFactory.getLogger(SessionInformationExpiredKimImpl.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
        logger.info("SessionInformationExpiredStrategyImpl");
        event.getResponse().setContentType(MediaType.APPLICATION_JSON_VALUE);
        event.getResponse().getWriter().write(mapper.writeValueAsString(new ResponseEntity<Object>("SessionInformationExpired", HttpStatus.BAD_REQUEST)));
    }

}