package com.example.springproject.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundGlobalException extends GlobalException {

    private HttpStatus status = HttpStatus.NOT_FOUND;

    public NotFoundGlobalException(String message) {
        super(message);
    }

    public NotFoundGlobalException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}