package net.oleksin.paymentsystem.model;


import net.oleksin.paymentsystem.account.Account;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "source_id")
  private Account source;
  
  @ManyToOne
  @JoinColumn(name = "destination_id")
  private Account destination;

  @Column(name = "amount")
  private BigDecimal amount;
  
  @Column(name = "reason")
  private String reason;
  
  @Column(name = "timestamp")
  private LocalDateTime timestamp;
  
  @Column(name = "status")
  private String status;
  
  public static class Builder {
    private Long id;
    private Account sourceAccount;
    private Account destinationAccount;
    private BigDecimal amount;
    private String reason;
    private String status;
  
    public Builder id(Long id) {
      this.id = id;
      return this;
    }
    
    public Builder sourceAccount(Account sourceAccount) {
      this.sourceAccount = sourceAccount;
      return this;
    }
  
    public Builder destinationAccount(Account destinationAccount) {
      this.destinationAccount = destinationAccount;
      return this;
    }
    
    public Builder amount(BigDecimal amount) {
      this.amount = amount;
      return this;
    }
    
    public Builder reason(String reason) {
      this.reason = reason;
      return this;
    }
  
    public Builder status(String status) {
      this.status = status;
      return this;
    }
    
    public Payment build() {
      return new Payment(this);
    }
  }
  
  public Payment() {
  }
  
  private Payment(Builder builder) {
    id = builder.id;
    source = builder.sourceAccount;
    destination = builder.destinationAccount;
    amount = builder.amount;
    reason = builder.reason;
    status = builder.status;
  }
  
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public Account getSource() {
    return source;
  }
  
  public void setSource(Account source) {
    this.source = source;
  }
  
  public Account getDestination() {
    return destination;
  }
  
  public void setDestination(Account destination) {
    this.destination = destination;
  }
  
  public BigDecimal getAmount() {
    return amount;
  }
  
  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
  
  public String getReason() {
    return reason;
  }
  
  public void setReason(String reason) {
    this.reason = reason;
  }
  
  public LocalDateTime getTimestamp() {
    return timestamp;
  }
  
  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }
  
  public String getStatus() {
    return status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
}
