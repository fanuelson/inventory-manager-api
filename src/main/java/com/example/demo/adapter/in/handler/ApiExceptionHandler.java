package com.example.demo.adapter.in.handler;

import com.example.demo.application.exception.IdempotencyInProgressException;
import com.example.demo.application.exception.IdempotencyKeyReusedException;
import com.example.demo.application.exception.InsufficientStockException;
import com.example.demo.application.exception.InventoryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(InventoryNotFoundException.class)
  ProblemDetail handleInventoryNotFound(InventoryNotFoundException e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
  }

  @ExceptionHandler(InsufficientStockException.class)
  ProblemDetail handleInsufficientStock(InsufficientStockException e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
  }

  @ExceptionHandler(IdempotencyKeyReusedException.class)
  ProblemDetail handleKeyReused(IdempotencyKeyReusedException e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, e.getMessage());
  }

  @ExceptionHandler(IdempotencyInProgressException.class)
  ProblemDetail handleInProgress(IdempotencyInProgressException e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
  }
}
