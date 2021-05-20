package kim.kin.exception;

import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 *
 * @author choky
 */
public class KkReqException extends RuntimeException{

    private Integer status = BAD_REQUEST.value();

    public KkReqException(String msg){
        super(msg);
    }

    public KkReqException(HttpStatus status, String msg){
        super(msg);
        this.status = status.value();
    }
}
