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

  public static AccountType fromString(String text) {
    for (AccountType b : AccountType.values()) {
      if (b.type.equalsIgnoreCase(text)) {
        return b;
      }
    }
    return null;
  }
}
