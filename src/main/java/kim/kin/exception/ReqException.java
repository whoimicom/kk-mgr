package kim.kin.exception;

import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 *
 * @author choky
 */
public class ReqException extends RuntimeException{

    private Integer status = BAD_REQUEST.value();

    public ReqException(String msg){
        super(msg);
    }

    public ReqException(HttpStatus status, String msg){
        super(msg);
        this.status = status.value();
    }
}
