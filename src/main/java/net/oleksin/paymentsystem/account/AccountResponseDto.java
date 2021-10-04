package net.oleksin.paymentsystem.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class AccountResponseDto {
  
  private final Set<AccountDto> accounts;
}
