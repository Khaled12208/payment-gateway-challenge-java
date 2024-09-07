package com.checkout.payment.gateway.controller;

import com.checkout.payment.gateway.model.ErrorResponse;
import com.checkout.payment.gateway.model.PaymentRequest;
import com.checkout.payment.gateway.model.PaymentResponse;
import com.checkout.payment.gateway.service.PaymentGatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@RestController("Payment Gateway")
@RequestMapping("/payment")
@Tag(name = "Payments Service", description = "Responsible for validating requests, storing card information and forwarding payment requests and accepting payment responses to and from the acquiring bank.")
public class PaymentGatewayController {

  private final PaymentGatewayService paymentGatewayService;

  @Autowired
  public PaymentGatewayController(PaymentGatewayService paymentGatewayService) {
    this.paymentGatewayService = paymentGatewayService;
  }

  @Operation(summary = "process a payment request")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "202", description = "payment created", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class)) }),
      @ApiResponse(responseCode = "400", description = "bad request",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
      @ApiResponse(responseCode = "500", description = "internal server error",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
  })
  @RequestMapping(value = "/submitpayment", method = {RequestMethod.POST})
  @PostMapping("/submitpayment")
  public ResponseEntity<PaymentResponse> submitPayment(@Valid @RequestBody PaymentRequest request) {
    PaymentResponse statusResponse = paymentGatewayService.processRequest(request);

    HttpStatus httpStatus;
    switch (statusResponse.getStatus()) {
      case Declined:
        httpStatus = HttpStatus.UNAUTHORIZED;
        break;
      case Rejected:
        httpStatus = HttpStatus.BAD_REQUEST;
        break;
      default:
        httpStatus = HttpStatus.ACCEPTED;
        break;
    }
    return new ResponseEntity<>(statusResponse, httpStatus);
  }


  @Operation(summary = "retrieve a payment request")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "get payment details", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class)) }),
      @ApiResponse(responseCode = "400", description = "bad request",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
      @ApiResponse(responseCode = "500", description = "internal server error",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
  })
  @GetMapping("/{paymentId}")
  public ResponseEntity<PaymentResponse> getPaymentDetailsByPaymentID(@Valid @PathVariable(required = true) String paymentId) {
      PaymentResponse paymentDetails = paymentGatewayService.getPaymentDetailsById(paymentId);
      return new ResponseEntity<>(paymentDetails,  HttpStatus.OK);
  }



}
