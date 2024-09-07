package com.checkout.payment.gateway.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerError extends EventProcessingException {
  public InternalServerError(String message) {
    super(message);
  }
}