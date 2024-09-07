package com.checkout.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;
import lombok.Data;


import java.time.YearMonth;

@Data
public class PaymentRequest {

  @NotNull
  @Size(min = 14, max = 19)
  @Pattern(regexp = "\\d+")
  @JsonProperty("card_number")
  @Schema(description = "The card number, between 14 and 19 digits.", example = "4111111111111111")
  private String cardNumber;


  @NotNull
  @Min(1)
  @Max(12)
  @JsonProperty("expiry_month")
  @Schema(description = "The expiry month of the card (1-12).", example = "08")
  private Integer expiryMonth;

  @NotNull
  @JsonProperty("expiry_year")
  @Schema(description = "The expiry year of the card.", example = "2025")
  private Integer expiryYear;

  @NotNull
  @Size(min = 3, max = 3)
  @Pattern(regexp = "[A-Z]{3}")
  @JsonProperty("currency")
  @Schema(description = "The currency code (ISO 4217).", example = "USD")
  private String currency;

  @NotNull
  @Min(1)
  @JsonProperty("amount")
  @Schema(description = "The amount in minor currency units (e.g., cents).", example = "1050")
  private Integer amount;

  @NotNull
  @Size(min = 3, max = 4)
  @Pattern(regexp = "\\d+")
  @JsonProperty("cvv")
  @Schema(description = "The CVV of the card.", example = "123")
  private String cvv;

  @Schema(hidden = true)
  public boolean isExpired() {
    return YearMonth.of(expiryYear, expiryMonth).isBefore(YearMonth.now());
  }
  public PaymentRequest() {
  }
  public PaymentRequest(
      String cardNumber,
     Integer expiryMonth,
      Integer expiryYear,
      String currency,
      Integer amount,
       String cvv
  ) {
    this.cardNumber = cardNumber;
    this.expiryMonth = expiryMonth;
    this.expiryYear = expiryYear;
    this.currency = currency;
    this.amount = amount;
    this.cvv = cvv;
  }
//  public String getCardNumber() {
//    return cardNumber;
//  }
//
//  public Integer getExpiryMonth() {
//    return expiryMonth;
//  }
//
//  public Integer getExpiryYear() {
//    return expiryYear;
//  }
//
//  public String getCurrency() {
//    return currency;
//  }
//
//  public Integer getAmount() {
//    return amount;
//  }
//
//  public String getCvv() {
//    return cvv;
//  }
//
//  public void setCardNumber(String cardNumber) {
//    this.cardNumber = cardNumber;
//  }
//
//  public void setExpiryMonth(Integer expiryMonth) {
//    this.expiryMonth = expiryMonth;
//  }
//
//  public void setExpiryYear(Integer expiryYear) {
//    this.expiryYear = expiryYear;
//  }
//
//  public void setCurrency(String currency) {
//    this.currency = currency;
//  }
//
//  public void setAmount(Integer amount) {
//    this.amount = amount;
//  }
//
//  public void setCvv(String cvv) {
//    this.cvv = cvv;
//  }
}
