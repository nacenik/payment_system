package net.oleksin.paymentsystem.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class AccountDto {
  private final Long id;
  private final String accountNumber;
  private final AccountType accountType;
  private final BigDecimal balance;
}
