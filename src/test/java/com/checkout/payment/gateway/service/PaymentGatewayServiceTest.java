package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.exception.PaymentNotFoundException;
import com.checkout.payment.gateway.model.BankResponse;
import com.checkout.payment.gateway.model.PaymentRequest;

import com.checkout.payment.gateway.model.PaymentResponse;
import com.checkout.payment.gateway.model.PaymentStatus;
import com.checkout.payment.gateway.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PaymentGatewayServiceTest {

  @InjectMocks
  private PaymentGatewayService paymentGatewayService;
  @Mock
  private RestTemplate restTemplate;
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    paymentGatewayService.targetApiUrl = "http://mockapi";
  }

  @Test
  void getPaymentDetailsById_paymentNotFound_shouldOK() {
    PaymentResponse mockPaymentResponse = new PaymentResponse();
    mockPaymentResponse.setPaymentID("1112");
    mockPaymentResponse.setStatus(PaymentStatus.Authorized);
    mockPaymentResponse.setAmount(1000);
    mockPaymentResponse.setLastFourCardDigits("1234");
    mockPaymentResponse.setExpiryDate("12/2025");
    mockPaymentResponse.setCurrency("USD");

    ResponseEntity<PaymentResponse> mockResponse = new ResponseEntity<>(mockPaymentResponse, HttpStatus.OK);

    Mockito.when(restTemplate.exchange(
        anyString(),                 // matcher for any URL string
        eq(HttpMethod.GET),          // matcher for HTTP GET method
        isNull(),                    // matcher for a null HttpEntity
        eq(PaymentResponse.class)    // matcher for ResponseEntity<PaymentResponse>
    )).thenReturn(mockResponse);

    PaymentResponse result = paymentGatewayService.getPaymentDetailsById("1112");

    assertNotNull(result);

  }

  @Test
  void getPaymentDetailsById_paymentNotFound_shouldThrowPaymentNotFoundException() {
    String invalidPaymentId = "2334-12334-12445-2244";

    Mockito.when(restTemplate.exchange(
        anyString(),                 // matcher for any URL string
        eq(HttpMethod.GET),          // matcher for HTTP GET method
        isNull(),                    // matcher for a null HttpEntity
        eq(PaymentResponse.class)    // matcher for ResponseEntity<PaymentResponse>
    )).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    assertThrows(PaymentNotFoundException.class, () -> paymentGatewayService.getPaymentDetailsById(invalidPaymentId));
  }

}