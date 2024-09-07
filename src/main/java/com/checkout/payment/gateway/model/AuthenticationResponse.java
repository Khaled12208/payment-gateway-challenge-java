package com.checkout.payment.gateway.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "login response")
public class AuthenticationResponse {

  private String message;

}
