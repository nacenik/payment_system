package net.oleksin.paymentsystem.account.jpa;

import lombok.AllArgsConstructor;
import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountService;
import net.oleksin.paymentsystem.accounttype.AccountTypeService;
import net.oleksin.paymentsystem.exception.AccountNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile({ "default", "springJpaProfile"})
@AllArgsConstructor
public class AccountServiceJpa implements AccountService {
  
  private final AccountRepository accountRepository;
  private final AccountTypeService accountTypeService;

  @Transactional
  @Override
  public Account getAccountById(Long id) {
    return accountRepository.findById(id)
            .orElseThrow(AccountNotFoundException::new);
  }

  @Transactional
  @Override
  public Account saveNewAccount(Account account) {
    if (account != null && account.getAccountType() != null) {
      account.setAccountType(accountTypeService.saveNewAccountType(account.getAccountType()));
      return accountRepository.save(account);
    }
    return null;
  }
}
