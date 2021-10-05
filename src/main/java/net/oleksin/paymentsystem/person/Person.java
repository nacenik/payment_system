package net.oleksin.paymentsystem.person;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.oleksin.paymentsystem.account.Account;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "persons")
@Builder
@Getter
@Setter
@NoArgsConstructor
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return Objects.equals(id, person.id) && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(accounts, person.accounts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, accounts);
  }
}
