package net.oleksin.paymentsystem.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Schema(description = "Request payment entity")
@AllArgsConstructor
@Builder
@Setter
@Getter
public class PaymentRequestDto {

  @Schema(description = "source account id", example = "2")
  private final Long sourceAccountId;
  @Schema(description = "destination account id", example = "3")
  private final Long destinationAccountId;
  @Schema(description = "amount", example = "500.00")
  private final BigDecimal amount;
  @Schema(description = "payment reason", example = "for example")
  private final String reason;
}
