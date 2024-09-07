package com.checkout.payment.gateway.model;


public enum PaymentStatus {
  Authorized("Authorized"),
  Declined("Declined"),
  Rejected("Rejected");

  private final String status;

  // Constructor
  PaymentStatus(String status) {
    this.status = status;
  }

  // Getter method to retrieve the status value
  public String getStatus() {
    return status;
  }

  @Override
  public String toString() {
    return status;
  }
}