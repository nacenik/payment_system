package net.oleksin.paymentsystem.payment;


import lombok.*;
import net.oleksin.paymentsystem.account.Account;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Payment {

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Payment payment = (Payment) o;
    return Objects.equals(id, payment.id) && Objects.equals(source, payment.source) && Objects.equals(destination, payment.destination) && Objects.equals(amount, payment.amount) && Objects.equals(reason, payment.reason) && Objects.equals(timestamp, payment.timestamp) && status == payment.status;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, source, destination, amount, reason, timestamp, status);
  }
}
