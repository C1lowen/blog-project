package com.users.users.controller;

import com.users.users.exceptios.AppError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialException(BadCredentialsException badCredentialsException){
        return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Некоректный логин или пароль"), HttpStatus.UNAUTHORIZED);
    }
}
