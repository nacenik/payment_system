package net.oleksin.paymentsystem.accounttype;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "types")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AccountType {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(name = "name")
  private String name;
}
