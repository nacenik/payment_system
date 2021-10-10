package net.oleksin.paymentsystem.account.jpa;

import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile({"springJpaProfile", "default"})
public class AccountServiceJpa implements AccountService {
  
  private final AccountRepository accountRepository;
  
  public AccountServiceJpa(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }
  
  @Transactional
  @Override
  public Account getAccountById(Long id) {
    return accountRepository.getById(id);
  }

}
