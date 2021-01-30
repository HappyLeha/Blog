package com.example.demo.controller;

import com.example.demo.exception.NotEnoughRightException;
import com.example.demo.exception.ResourceAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NotEnoughRight {

    @ResponseBody
    @ExceptionHandler(NotEnoughRightException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String NotEnoughRightHandler (NotEnoughRightException ex) {
        return ex.getMessage();
    }
}
