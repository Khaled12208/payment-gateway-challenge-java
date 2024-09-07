package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.exception.InternalServerError;
import com.checkout.payment.gateway.exception.PaymentNotFoundException;
import com.checkout.payment.gateway.model.BankResponse;
import com.checkout.payment.gateway.model.HttpRequest;
import com.checkout.payment.gateway.model.HttpResponse;
import com.checkout.payment.gateway.model.MockServerExpectation;
import com.checkout.payment.gateway.model.PaymentRequest;
import com.checkout.payment.gateway.model.PaymentResponse;
import com.checkout.payment.gateway.model.PaymentStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentGatewayService {
  private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayService.class);

  @Value("${target.api.url}")
  String targetApiUrl;

  private RestTemplate restTemplate;

  @Autowired
  public PaymentGatewayService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public PaymentResponse processRequest(PaymentRequest paymentRequest) {
    if (isExpired(paymentRequest.getExpiryDate())) {
      return createRejectedResponse();
    }
    return processValidData(paymentRequest);
  }

  public PaymentResponse getPaymentDetailsById(String paymentId) {
    String resourceName = "/payments/" + paymentId;

    HttpHeaders headers = createHeaders();
    try {
      ResponseEntity<PaymentResponse> response = restTemplate.exchange(
          targetApiUrl + resourceName,
          HttpMethod.GET,
          null,
          PaymentResponse.class
      );

      return response.getBody();
    } catch (HttpClientErrorException.NotFound ex) {
      throw new PaymentNotFoundException("Payment with ID " + paymentId + " not found.");
    } catch (HttpClientErrorException ex) {
      throw new PaymentNotFoundException("Error occurred while retrieving payment details.");
    } catch (HttpServerErrorException ex) {
      throw new InternalServerError("Internal server error");
    }
  }



  private PaymentResponse createRejectedResponse() {
    PaymentResponse response = new PaymentResponse();
    response.setStatus(PaymentStatus.Rejected);
    return response;
  }

  private PaymentResponse processValidData(PaymentRequest paymentRequest) {

    String resourceName= "/payments";

    HttpHeaders headers = createHeaders();
    HttpEntity<PaymentRequest> entity = new HttpEntity<>(paymentRequest, headers);
    ResponseEntity<BankResponse> response;

    try {
      response = restTemplate.exchange(
          targetApiUrl+resourceName,
          HttpMethod.POST,
          entity,
          BankResponse.class
      );
    } catch (HttpClientErrorException.NotFound ex) {
      LOG.error("Bank details not found", ex);
      throw new PaymentNotFoundException("Bank details not found");
    } catch (Exception ex) {
      LOG.error("Error while processing payment request", ex);
      throw new PaymentNotFoundException("An error occurred while processing payment");
    }
    if (response.getStatusCode().is2xxSuccessful()) {
      return createPaymentResponse(paymentRequest, response.getBody());
    } else {
      throw new PaymentNotFoundException("Payment details didn't match");
    }
  }

  private HttpHeaders createHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }

  private PaymentResponse createPaymentResponse(PaymentRequest paymentRequest, BankResponse bankResponse) {
    PaymentResponse response = new PaymentResponse();
    String paymentID = UUID.randomUUID().toString();
    if("false".equalsIgnoreCase(bankResponse.getAuthorized()))
    {
      response.setStatus(PaymentStatus.Declined);
    }else
    {
      response.setPaymentID(paymentID);
      response.setStatus(PaymentStatus.Authorized);
      response.setAmount(paymentRequest.getAmount());
      response.setLastFourCardDigits(maskCardNumber(paymentRequest.getCardNumber()));
      response.setExpiryDate(paymentRequest.getExpiryDate());
      response.setCurrency(paymentRequest.getCurrency());
    }
    setMocks(response,paymentID);
    return response;
  }


  private static String maskCardNumber(String cardNumber) {
    int lengthToMask = cardNumber.length() - 4;
    StringBuilder maskedNumber = new StringBuilder();

    for (int i = 0; i < lengthToMask; i++) {
      maskedNumber.append('*');
    }
    maskedNumber.append(cardNumber.substring(lengthToMask));
    return maskedNumber.toString();
  }

  private boolean isExpired(String expiryDate) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
      YearMonth expiryYearMonth = YearMonth.parse(expiryDate, formatter);
      return expiryYearMonth.isBefore(YearMonth.now());
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid expiry date format. Expected MM/YY.");
    }
  }

  private void setMocks(PaymentResponse response,String cardNumber)
  {
    String resourceName= "/mockserver/expectation";
    ObjectMapper objectMapper = new ObjectMapper();

    Map<String, List<String>> queryStringParameters = new HashMap<>();
    queryStringParameters.put("paymentID", Collections.singletonList(response.getPaymentID()));

    MockServerExpectation mockRequest = new MockServerExpectation();
    HttpRequest httpRequest = new HttpRequest();
    httpRequest.setPath("/payments/"+cardNumber);
    httpRequest.setMethod("GET");

    String jsonString = "";
    try {
       jsonString = objectMapper.writeValueAsString(response);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    mockRequest.setHttpRequest(httpRequest);
    mockRequest.setHttpRequest(httpRequest);

    HttpResponse httpResponse= new HttpResponse();
    httpResponse.setBody(jsonString);
    httpResponse.setStatusCode(200);
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    httpResponse.setHeaders(headers);
    mockRequest.setHttpResponse(httpResponse);

    HttpHeaders reqHeader = createHeaders();
    HttpEntity<MockServerExpectation> entity = new HttpEntity<>(mockRequest, reqHeader);
     restTemplate.exchange(
          targetApiUrl+resourceName,
          HttpMethod.PUT,
          entity,
          String.class
      );

  }



}
