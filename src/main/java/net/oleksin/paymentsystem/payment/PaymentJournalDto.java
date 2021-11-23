package net.oleksin.paymentsystem.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import net.oleksin.paymentsystem.person.PersonResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Response payment journal entity")
@Data
@Builder
@ToString
public class PaymentJournalDto {

  @Schema(description = "payment id", example = "1")
  private Long paymentId;
  @Schema(description = "time when payment was created", example = "2020-08-25 13:18:54")
  private LocalDateTime timestamp;
  @Schema(description = "source account number", example = "123456789A1")
  private String sourceAccountNumber;
  @Schema(description = "destination account number", example = "987654321B1")
  private String destinationAccountNumber;
  @Schema(description = "amount", example = "5000.00")
  private BigDecimal amount;
  @Schema(description = "information about payer")
  private PersonResponseDto payer;
  @Schema(description = "information about recipient")
  private PersonResponseDto recipient;
}
