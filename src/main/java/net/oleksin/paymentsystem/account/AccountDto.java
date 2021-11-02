package net.oleksin.paymentsystem.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Schema(description = "Account entity")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto implements Serializable {

  @Schema(description = "account id", example = "123")
  private Long id;

  @Schema(description = "account number", example = "1232312A2")
  private String accountNumber;

  @Schema(description = "account type", example = "simple/card")
  private String accountType;

  @Schema(description = "amount", example = "5000")
  private BigDecimal balance;
}
