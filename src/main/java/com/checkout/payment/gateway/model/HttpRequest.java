package com.checkout.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class HttpRequest {

  @JsonProperty("path")
  private String path;

  @JsonProperty("method")
  private String method;


}
