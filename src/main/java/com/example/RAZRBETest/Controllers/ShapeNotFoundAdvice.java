package com.example.RAZRBETest.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ShapeNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(ShapeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String shapeNotFoundHandler(ShapeNotFoundException ex){
        return ex.getMessage();
    }
}