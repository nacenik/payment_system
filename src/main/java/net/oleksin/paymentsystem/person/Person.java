package net.oleksin.paymentsystem.person;

import lombok.*;
import net.oleksin.paymentsystem.account.Account;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "persons")
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class Person {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(name = "first_name")
  private String firstName;
  
  @Column(name = "last_name")
  private String lastName;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
  private Set<Account> accounts = new HashSet<>();
}
