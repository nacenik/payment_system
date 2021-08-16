package net.oleksin.paymentsystem.account;

import net.oleksin.paymentsystem.model.Payment;
import net.oleksin.paymentsystem.person.Person;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "accounts")
public class Account {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(name = "account_number")
  private Long accountNumber;
  
  @Column(name = "account_type")
  private String accountType;
  
  @Column(name = "balance")
  private BigDecimal balance;
  
  @ManyToOne
  @JoinColumn(name = "person_id")
  private Person person;
  
  @OneToMany(mappedBy = "source")
  private List<Payment> source = new ArrayList<>();
  
  @OneToMany(mappedBy = "destination")
  private List<Payment> destination = new ArrayList<>();
  
  private static class Builder {
    private Long id;
    private Long accountNumber;
    private String accountType;
    private BigDecimal balance;
    private Person person;
  
    public Builder id(Long id) {
      this.id = id;
      return this;
    }
    
    public Builder accountNumber(Long accountNumber) {
      this.accountNumber = accountNumber;
      return this;
    }
    
    public Builder accountNumber(String accountType) {
      this.accountType = accountType;
      return this;
    }
    
    public Builder accountNumber(BigDecimal balance) {
      this.balance = balance;
      return this;
    }
  
    public Builder person(Person person) {
      this.person = person;
      return this;
    }
    
    public Account build() {
      return new Account(this);
    }
  }
  
  public Account() {
  }
  
  private Account(Builder builder) {
    this.id = builder.id;
    this.accountNumber = builder.accountNumber;
    this.accountType = builder.accountType;
    this.balance = builder.balance;
    this.person = builder.person;
  }
  
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public Long getAccountNumber() {
    return accountNumber;
  }
  
  public void setAccountNumber(Long accountNumber) {
    this.accountNumber = accountNumber;
  }
  
  public String getAccountType() {
    return accountType;
  }
  
  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }
  
  public BigDecimal getBalance() {
    return balance;
  }
  
  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }
  
  public Person getPerson() {
    return person;
  }
  
  public void setPerson(Person person) {
    this.person = person;
  }
  
  public List<Payment> getSource() {
    return source;
  }
  
  public void setSource(List<Payment> source) {
    this.source = source;
  }
  
  public List<Payment> getDestination() {
    return destination;
  }
  
  public void setDestination(List<Payment> destination) {
    this.destination = destination;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Account account = (Account) o;
    return Objects.equals(id, account.id) && Objects.equals(accountNumber, account.accountNumber) && Objects.equals(accountType, account.accountType) && Objects.equals(balance, account.balance) && Objects.equals(person, account.person);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(id, accountNumber, accountType, balance, person);
  }
}
