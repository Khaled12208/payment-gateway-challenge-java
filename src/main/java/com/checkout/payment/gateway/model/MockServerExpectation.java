package com.checkout.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class MockServerExpectation {
  @JsonProperty("httpRequest")
  private HttpRequest httpRequest;

  @JsonProperty("httpResponse")
  private HttpResponse httpResponse;

}
