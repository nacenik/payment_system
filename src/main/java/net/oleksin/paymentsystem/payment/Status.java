package net.oleksin.paymentsystem.payment;

public enum Status {
  ok("ok"),
  error("error");

  private final String name;

  Status(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static Status getByName(String name) {
    for(Status status : values()){
      if(status.getName().equals(name)){
        return status;
      }
    }

    throw new IllegalArgumentException(name + " is not a valid PropName");
  }
}
