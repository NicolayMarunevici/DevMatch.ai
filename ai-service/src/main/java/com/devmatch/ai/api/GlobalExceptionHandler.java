package com.devmatch.ai.api;

import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> badRequest(IllegalArgumentException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("bad_request", e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> invalid(MethodArgumentNotValidException e) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err("validation_error", e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> generic(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err("internal_error", e.getMessage()));
  }

  private Map<String,Object> err(String code, String msg) {
    return Map.of("timestamp", Instant.now().toString(), "error", code, "message", msg);
  }
}
