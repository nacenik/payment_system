package net.oleksin.paymentsystem.account;

import lombok.*;
import net.oleksin.paymentsystem.accounttype.AccountType;
import net.oleksin.paymentsystem.person.Person;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Account implements Serializable {
  
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
}
