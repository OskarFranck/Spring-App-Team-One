package com.example.springproject.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AlreadyExistsGlobalException extends GlobalException {

    private HttpStatus status = HttpStatus.FORBIDDEN;

    public AlreadyExistsGlobalException(String message) {
        super(message);
    }

    public AlreadyExistsGlobalException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}
