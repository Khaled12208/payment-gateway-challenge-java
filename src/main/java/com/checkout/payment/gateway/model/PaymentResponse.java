package com.checkout.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;


@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Response status")
@Component
public class PaymentResponse {

  @JsonProperty("id")
  @Schema(description = "Unique identifier for the payment", example = "1234CD-1344AS-34A55-12344D")
  private String paymentID;

  @JsonProperty("status")
  @Schema(description = "Status of the payment", example = "Authorized")
  private PaymentStatus status;

  @JsonProperty("card_number")
  @Schema(description = "last 4 digits of the card number", example = "**********543")
  private String lastFourCardDigits;

  @JsonProperty("expiry_date")
  @Schema(description = "The expiry date of the card in MM/YYYY format.", example = "03/2024")
  private String expiryDate;

  @JsonProperty("currency")
  @Schema(description = "The currency code (ISO 4217).", example = "USD")
  private String currency;

  @JsonProperty("amount")
  @Schema(description = "The amount in minor currency units (e.g., cents).", example = "1050")
  private Integer amount;

}
