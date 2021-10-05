package net.oleksin.paymentsystem.account;

import lombok.*;
import net.oleksin.paymentsystem.person.Person;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Account {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(name = "account_number")
  private String accountNumber;
  
  @Column(name = "account_type")
  @Enumerated(EnumType.STRING)
  private AccountType accountType;
  
  @Column(name = "balance")
  private BigDecimal balance;
  
  @ManyToOne
  @JoinColumn(name = "person_id")
  private Person person;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Account account = (Account) o;
    return Objects.equals(id, account.id) && Objects.equals(accountNumber, account.accountNumber) && accountType == account.accountType && Objects.equals(balance, account.balance) && Objects.equals(person, account.person);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, accountNumber, accountType, balance, person);
  }

}
