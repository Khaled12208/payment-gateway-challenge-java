package com.checkout.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;
import javax.validation.constraints.*;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
@Component
public class PaymentRequest {

  @NotNull
  @Size(min = 14, max = 19)
  @Pattern(regexp = "\\d+")
  @JsonProperty("card_number")
  @Schema(description = "The card number, between 14 and 19 digits.", example = "4111111111111111")
  private String cardNumber;

  @NotNull
  @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{4}", message = "Expiry date must be in the format MM/YYYY and within a valid range.")
  @JsonProperty("expiry_date")
  @Schema(description = "The expiry date of the card in MM/YYYY format.", example = "03/2024")
  private String expiryDate;

  @NotNull
  @Size(min = 3, max = 3)
  @Pattern(regexp = "[A-Z]{3}")
  @JsonProperty("currency")
  @Schema(description = "The currency code (ISO 4217).", example = "USD")
  private String currency;

  @NotNull
  @Min(1)
  @JsonProperty("amount")
  @Positive(message = "The amount value must be positive")
  @Schema(description = "The amount in minor currency units (e.g., cents).", example = "1050")
  private Integer amount;

  @NotNull
  @Min(3)
  @JsonProperty("cvv")
  @Schema(description = "The CVV of the card.", example = "123")
  private Integer cvv;

  public boolean isExpired(String expiryDate) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
      YearMonth expiryYearMonth = YearMonth.parse(expiryDate, formatter);
      return expiryYearMonth.isBefore(YearMonth.now());
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid expiry date format. Expected MM/YY.");
    }
  }


}
