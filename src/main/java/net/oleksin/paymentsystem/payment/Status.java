package net.oleksin.paymentsystem.payment;

public enum Status {
  OK("ok"),
  ERROR("error");
  
  private final String status;
  
  Status(String status) {
    this.status = status;
  }
  
  public String getStatus() {
    return status;
  }
}
