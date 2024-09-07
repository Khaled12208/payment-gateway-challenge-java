package com.checkout.payment.gateway.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.checkout.payment.gateway.model.PaymentRequest;
import com.checkout.payment.gateway.model.PaymentResponse;
import com.checkout.payment.gateway.model.PaymentStatus;
import com.checkout.payment.gateway.service.PaymentGatewayService;
import com.checkout.payment.gateway.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class PaymentGatewayControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private PaymentGatewayService paymentGatewayService;
  @Autowired
  private ObjectMapper objectMapper;

  @ParameterizedTest
  @MethodSource("provideAuthorizedPaymentRequestsAndResponses")
  @WithMockUser(username = "admin", password = "password", roles = {"ADMIN"})
  public void shouldReturnAcceptedStatusAndPaymentIdWhenPaymentIsAuthorized(PaymentRequest paymentRequest, PaymentResponse expectedResponse) throws Exception {
    when(paymentGatewayService.processRequest(any(PaymentRequest.class))).thenReturn(expectedResponse);
    String body = objectMapper.writeValueAsString(paymentRequest);

    mvc.perform(post("/payment/submitpayment")
            .contentType("application/json")
            .content(body))
        .andExpect(MockMvcResultMatchers.status().isAccepted())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.status").value(expectedResponse.getStatus().name()))
        .andExpect(jsonPath("$.amount").value(expectedResponse.getAmount()))
        .andExpect(jsonPath("$.card_number").value(expectedResponse.getLastFourCardDigits()));

  }
  static Stream<Arguments> provideAuthorizedPaymentRequestsAndResponses() {
    return Stream.of(
        Arguments.of(
            Utils.createPaymentRequest("2222405343248877", "04/2025", "GBP", 2000, 789),
            Utils.createPaymentResponse(UUID.randomUUID().toString(),"************8877", "04/2025", "GBP", 2000, PaymentStatus.Authorized)
        ),
        Arguments.of(
            Utils.createPaymentRequest("4111111111111111", "06/2023", "USD", 5000, 123),
            Utils.createPaymentResponse(UUID.randomUUID().toString(),"1111", "06/2023", "USD", 5000, PaymentStatus.Authorized)
        )
    );
  }


  @ParameterizedTest
  @MethodSource("provideUnAuthorizedPaymentRequestsAndResponses")
  @WithMockUser(username = "admin", password = "password", roles = {"ADMIN"})
  public void shouldReturnAcceptedStatusAndPaymentIdWhenPaymentIsUnAuthorized(PaymentRequest paymentRequest, PaymentResponse expectedResponse) throws Exception {
    when(paymentGatewayService.processRequest(any(PaymentRequest.class))).thenReturn(expectedResponse);
    String body = objectMapper.writeValueAsString(paymentRequest);

    mvc.perform(post("/payment/submitpayment")
            .contentType("application/json")
            .content(body))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Declined"));
  }

  static Stream<Arguments> provideUnAuthorizedPaymentRequestsAndResponses() {
    return Stream.of(
        Arguments.of(
            Utils.createPaymentRequest("4111111111111111", "12/2023", "USD", 5000, 456),
            Utils.createPaymentResponse("","" ,"","",0,PaymentStatus.Declined)
        )
    );
  }


  @ParameterizedTest
  @MethodSource("provideRejectedPaymentRequestsAndResponses")
  @WithMockUser(username = "admin", password = "password", roles = {"ADMIN"})
  public void shouldReturnAcceptedStatusAndPaymentIdWhenPaymentIsRejected(PaymentRequest paymentRequest,  PaymentResponse expectedResponse) throws Exception {
   when(paymentGatewayService.processRequest(any(PaymentRequest.class))).thenReturn(expectedResponse);
    String body = objectMapper.writeValueAsString(paymentRequest);

    mvc.perform(post("/payment/submitpayment")
            .contentType("application/json")
            .content(body))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Rejected"));
  }

  static Stream<Arguments> provideRejectedPaymentRequestsAndResponses() {
    return Stream.of(
        Arguments.of(
            Utils.createPaymentRequest("1234567890123456", "10/2026", "EUR", 1000, 456),
            Utils.createPaymentResponse("","" ,"","",0,PaymentStatus.Rejected))
    );
  }


}
