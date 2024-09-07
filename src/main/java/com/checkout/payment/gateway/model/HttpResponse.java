package com.checkout.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;

@Data
public class HttpResponse {

  @JsonProperty("statusCode")
  private int statusCode;

  @JsonProperty("headers")
  private Map<String, String> headers;

  @JsonProperty("body")
  private String body;

}
