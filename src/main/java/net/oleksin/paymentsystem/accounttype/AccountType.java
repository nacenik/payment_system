package net.oleksin.paymentsystem.accounttype;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "types")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class AccountType implements Serializable {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(name = "name")
  private String name;
}
