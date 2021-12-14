package net.oleksin.paymentsystem.person;

import lombok.*;
import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.security.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "persons")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(name = "first_name")
  private String firstName;
  
  @Column(name = "last_name")
  private String lastName;

  @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
  private List<Account> accounts = new ArrayList<>();

  @Override
  public String toString() {
    return "Person{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", accounts=" + accounts +
            '}';
  }
}
