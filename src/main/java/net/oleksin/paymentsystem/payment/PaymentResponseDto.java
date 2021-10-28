package net.oleksin.paymentsystem.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Response payment entity")
@AllArgsConstructor
@Builder
@Setter
@Getter
public class PaymentResponseDto {

  @Schema(description = "payment id", example = "123")
  private final Long id;
  @Schema(description = "payment status", example = "error")
  private final Status status;
}
