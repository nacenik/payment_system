package net.oleksin.paymentsystem.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Response payment entity")
@Builder
@Data
public class PaymentResponseDto {

  @Schema(description = "payment id", example = "123")
  private final Long id;
  @Schema(description = "payment status", example = "error")
  private final Status status;
}
