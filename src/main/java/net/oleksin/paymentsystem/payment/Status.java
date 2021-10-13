package net.oleksin.paymentsystem.payment;

public enum Status {
  OK("ok"),
  ERROR("error");
  
  private final String name;
  
  Status(String name) {
    this.name = name;
  }
  
  public String getName() {
    return name;
  }
}
