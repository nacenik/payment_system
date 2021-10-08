package net.oleksin.paymentsystem.account;

public enum AccountType {
  simpleCard("simpleCard") {
    @Override
    public String toString() {
      return this.getAccountType();
    }
  },
  creditCard("creditCard") {
    @Override
    public String toString() {
      return this.getAccountType();
    }
  };
  
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
