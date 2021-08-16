package net.oleksin.paymentsystem.person;

import net.oleksin.paymentsystem.account.Account;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "persons")
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
  
  
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public String getFirstName() {
    return firstName;
  }
  
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public String getLastName() {
    return lastName;
  }
  
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  
  public Set<Account> getAccounts() {
    return accounts;
  }
  
  public void setAccounts(Set<Account> accounts) {
    this.accounts = accounts;
  }
  
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
