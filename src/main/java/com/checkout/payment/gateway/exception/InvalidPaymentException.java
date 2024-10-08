package com.checkout.payment.gateway.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPaymentException extends EventProcessingException {
  public InvalidPaymentException(String message) {
    super(message);
  }
}