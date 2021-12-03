package com.example.springproject.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExceptionResponse {

    private String message;
    private LocalDateTime dateTime;

}