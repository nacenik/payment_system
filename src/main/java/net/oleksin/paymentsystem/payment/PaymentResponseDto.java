package net.oleksin.paymentsystem.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class PaymentResponseDto {
  private final Long id;
  private final Status status;
}
