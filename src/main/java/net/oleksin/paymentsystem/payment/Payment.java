package net.oleksin.paymentsystem.payment;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.oleksin.paymentsystem.account.Account;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable, Persistable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "source_id")
  private Account source;
  
  @ManyToOne(fetch = FetchType.LAZY)
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
  public boolean isNew() {
    return id == null;
  }
}
