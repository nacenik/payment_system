package net.oleksin.paymentsystem.payment;


import lombok.*;
import net.oleksin.paymentsystem.account.Account;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Payment implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "source_id")
  private Account source;
  
  @ManyToOne
  @JoinColumn(name = "destination_id")
  private Account destination;

  @Column(name = "amount")
  private BigDecimal amount;
  
  @Column(name = "reason")
  private String reason;
  
  @Column(name = "timestamp")
  private LocalDateTime timestamp;
  
  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;
}
