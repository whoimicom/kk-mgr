package com.whoimi.exception;

import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 *
 * @author whoimi
 */
public class ReqWiiException extends RuntimeException{

    private Integer status = BAD_REQUEST.value();

    public ReqWiiException(String msg){
        super(msg);
    }

    public ReqWiiException(HttpStatus status, String msg){
        super(msg);
        this.status = status.value();
    }
}
