package com.example.springproject.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException{

    private HttpStatus status;

    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
