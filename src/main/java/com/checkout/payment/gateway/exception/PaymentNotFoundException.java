package com.checkout.payment.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PaymentNotFoundException extends EventProcessingException {
  public PaymentNotFoundException(String id) {
    super("Payment with ID " + id + " not found");
  }
}