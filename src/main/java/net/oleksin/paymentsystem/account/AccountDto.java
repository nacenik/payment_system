package net.oleksin.paymentsystem.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Schema(description = "Account entity")
@AllArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode
public class AccountDto {

  @Schema(accessMode = Schema.AccessMode.READ_ONLY)
  private final Long id;
  @Schema(description = "account number", example = "1232312A2")
  private final String accountNumber;
  @Schema(description = "account type", example = "simple/card")
  private final String accountType;
  @Schema(description = "amount", example = "5000")
  private final BigDecimal balance;
}
