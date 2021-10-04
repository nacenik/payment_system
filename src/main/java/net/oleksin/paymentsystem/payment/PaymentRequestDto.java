package net.oleksin.paymentsystem.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class PaymentRequestDto {
  
  private final Long sourceAccountId;
  private final Long destinationAccountId;
  private final BigDecimal amount;
  private final String reason;
}
