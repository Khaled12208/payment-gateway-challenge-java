package com.checkout.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response status")
public class BankResponse {

  @JsonProperty("authorization_code")
  @Schema(description = "Unique identifier for the payment", example = "12345abcde")
  private String authorizationCode;

  @JsonProperty("authorized")
  @Schema(description = "Status of the payment", example = "COMPLETED")
  private String authorized;

}
