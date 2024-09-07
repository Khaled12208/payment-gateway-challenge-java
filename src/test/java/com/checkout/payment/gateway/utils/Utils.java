package com.checkout.payment.gateway.utils;

import com.checkout.payment.gateway.model.PaymentRequest;
import com.checkout.payment.gateway.model.PaymentResponse;
import com.checkout.payment.gateway.model.PaymentStatus;
import java.util.UUID;

public class Utils {


  public static PaymentRequest createPaymentRequest(String cardNumber, String expiryDate, String currency, int amount, int cvv) {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setCardNumber(cardNumber);
    paymentRequest.setExpiryDate(expiryDate);
    paymentRequest.setCurrency(currency);
    paymentRequest.setAmount(amount);
    paymentRequest.setCvv(cvv);
    return paymentRequest;
  }

  public static PaymentResponse createPaymentResponse(String id, String lastFourDigits, String expiryDate, String currency, int amount, PaymentStatus status) {
    PaymentResponse paymentResponse = new PaymentResponse();
    paymentResponse.setPaymentID(id);
    paymentResponse.setLastFourCardDigits(lastFourDigits);
    paymentResponse.setExpiryDate(expiryDate);
    paymentResponse.setCurrency(currency);
    paymentResponse.setAmount(amount);
    paymentResponse.setStatus(status);
    return paymentResponse;
  }

}
