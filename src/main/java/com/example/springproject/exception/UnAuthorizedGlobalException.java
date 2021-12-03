package com.example.springproject.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnAuthorizedGlobalException extends GlobalException {

    private HttpStatus status = HttpStatus.UNAUTHORIZED;

    public UnAuthorizedGlobalException(String message) {
        super(message);
    }

    public UnAuthorizedGlobalException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}
