package com.checkout.payment.gateway.exception;

import com.checkout.payment.gateway.model.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
    String traceId = UUID.randomUUID().toString();
    ErrorResponse errorResponse = new ErrorResponse(
        "An unexpected error occurred: " + ex.getMessage(),
        LocalDateTime.now(),
        traceId
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }


  @ExceptionHandler(InvalidPaymentException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleInvalidPaymentException(InvalidPaymentException ex) {
    String traceId = UUID.randomUUID().toString();
    ErrorResponse errorResponse = new ErrorResponse(
        ex.getMessage(),
        LocalDateTime.now(),
        traceId
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(PaymentNotFoundException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> handlePaymentNotFoundException(PaymentNotFoundException ex) {
    String traceId = UUID.randomUUID().toString();
    ErrorResponse errorResponse = new ErrorResponse(
        ex.getMessage(),
        LocalDateTime.now(),
        traceId
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
    String traceId = UUID.randomUUID().toString();
    ErrorResponse errorResponse = new ErrorResponse(
        "Access denied: " + ex.getMessage(),
        LocalDateTime.now(),
        traceId
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
  }
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse>  handleConstraintViolationException(
      ConstraintViolationException ex) {
    String traceId = UUID.randomUUID().toString();
    Map<String, String> errors = new HashMap<>();
    ex.getConstraintViolations().forEach(violation -> {
      errors.put("path", violation.getPropertyPath().toString());
      errors.put("prop", violation.getPropertyPath().toString());
      errors.put("message", violation.getMessage());
    });

    StringBuilder errorMessages = new StringBuilder();
    errors.forEach((field, message) -> {
      errorMessages.append(field).append(": ").append(message).append("; ");
    });


    ErrorResponse errorResponse = new ErrorResponse(
        errorMessages.toString().trim(),
        LocalDateTime.now(),
        traceId
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    String traceId = UUID.randomUUID().toString();
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
      String fieldName = fieldError.getField();
      String errorMessage = fieldError.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    StringBuilder errorMessages = new StringBuilder();
    errors.forEach((field, message) -> {
      errorMessages.append(field).append(": ").append(message).append("; ");
    });

    ErrorResponse errorResponse = new ErrorResponse(
        errorMessages.toString().trim(),
        LocalDateTime.now(),
        traceId
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
