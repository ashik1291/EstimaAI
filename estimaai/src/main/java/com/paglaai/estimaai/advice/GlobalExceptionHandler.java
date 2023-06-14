package com.paglaai.estimaai.advice;

import com.paglaai.estimaai.exception.DynamicReportException;
import com.paglaai.estimaai.exception.NoExportTypeFoundException;
import com.paglaai.estimaai.exception.ResourceAlreadyExistsException;
import com.paglaai.estimaai.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(NoExportTypeFoundException.class)
  public ResponseEntity<String> handleNoExportTypeFoundException(NoExportTypeFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(DynamicReportException.class)
  public ResponseEntity<String> handleDynamicReportException(DynamicReportException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body("Bad Credentials");
  }

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<String> handleException(ResourceAlreadyExistsException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(ex.getMessage());
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleBadCredentialsException(RuntimeException ex) {
    ex.printStackTrace();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception ex) {
    ex.printStackTrace();
    return ResponseEntity.badRequest().body("Something went wrong");
  }
}
