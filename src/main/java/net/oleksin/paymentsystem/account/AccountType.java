package net.oleksin.paymentsystem.account;

public enum AccountType {
  SIMPLE_CARD("simple/card"),
  CREDIT_CARD("credit/card");
  
  private final String type;
  
  AccountType(String type) {
    this.type = type;
  }
  
  public String getAccountType() {
    return type;
  }
}
