package com.checkout.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;



public class ErrorResponse {

  @JsonProperty("error_message")
  @Schema(description = "A human-readable message describing the error.", example = "Invalid payment request")
  private String errorMessage;

  @JsonProperty("timestamp")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(description = "The timestamp when the error occurred.", example = "2024-08-30T12:34:56")
  private LocalDateTime timestamp;

  @JsonProperty("trace_id")
  @Schema(description = "A unique identifier for tracking the request across systems.", example = "abc123xyz789")
  private String traceId;
  // Custom constructor for convenience if needed

  public ErrorResponse(String errorMessage, LocalDateTime timestamp, String traceId) {
    this.errorMessage = errorMessage;
    this.timestamp = timestamp;
    this.traceId = traceId;
  }
  public ErrorResponse(){}

  public String getErrorMessage() {
    return errorMessage;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public String getTraceId() {
    return traceId;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public void setTraceId(String traceId) {
    this.traceId = traceId;
  }
}
