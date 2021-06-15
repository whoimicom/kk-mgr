package kim.kin.exception;

import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 *
 * @author choky
 */
public class ReqKimException extends RuntimeException{

    private Integer status = BAD_REQUEST.value();

    public ReqKimException(String msg){
        super(msg);
    }

    public ReqKimException(HttpStatus status, String msg){
        super(msg);
        this.status = status.value();
    }
}
