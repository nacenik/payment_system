package net.oleksin.paymentsystem.payment;

import lombok.Builder;
import lombok.Data;
import net.oleksin.paymentsystem.person.PersonResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentJournalDto {
  
  private Long paymentId;
  private LocalDateTime timestamp;
  private String sourceAccountNumber;
  private String destinationAccountNumber;
  private BigDecimal amount;
  private PersonResponseDto payer;
  private PersonResponseDto recipient;
}
