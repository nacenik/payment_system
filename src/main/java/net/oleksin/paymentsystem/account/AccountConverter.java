package net.oleksin.paymentsystem.account;

import net.oleksin.paymentsystem.Converter;
import net.oleksin.paymentsystem.accounttype.AccountType;
import org.springframework.stereotype.Component;

@Component
public class AccountConverter implements Converter<AccountDto, AccountDto, Account> {
  @Override
  public AccountDto toResponseDto(Account account) {
    return AccountDto.builder()
            .id(account.getId())
            .accountType(account.getAccountType().getName())
            .accountNumber(account.getAccountNumber())
            .balance(account.getBalance())
            .build();
  }
  
  @Override
  public Account fromRequestDto(AccountDto accountDto) {
    return Account.builder()
            .id(accountDto.getId())
            .accountType(AccountType.builder().name(accountDto.getAccountType()).build())
            .accountNumber(accountDto.getAccountNumber())
            .balance(accountDto.getBalance())
            .build();
  }
}
