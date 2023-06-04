package com.paglaai.estimaai.aop;

import com.paglaai.estimaai.exception.DynamicReportException;
import com.paglaai.estimaai.exception.NoExportTypeFoundException;
import com.paglaai.estimaai.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NoExportTypeFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(NoExportTypeFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DynamicReportException.class)
    public ResponseEntity<String> handleUserNotFoundException(DynamicReportException ex) {
        return ResponseEntity.status(HttpStatus.PROCESSING).body(ex.getMessage());
    }
}

