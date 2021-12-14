package net.oleksin.paymentsystem.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.oleksin.paymentsystem.accounttype.AccountType;
import net.oleksin.paymentsystem.person.Person;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable, Persistable<Long> {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(name = "account_number")
  private String accountNumber;
  
  @ManyToOne
  @JoinColumn(name = "type_id")
  private AccountType accountType;
  
  @Column(name = "balance")
  private BigDecimal balance;
  
  @ManyToOne
  @JoinColumn(name = "person_id")
  private Person person;

  @Override
  public boolean isNew() {
    return id == null;
  }

  @Override
  public String toString() {
    return "Account{" +
            "id=" + id +
            ", accountNumber='" + accountNumber +
            ", accountType=" + accountType +
            ", balance=" + balance +
            '}';
  }
}
