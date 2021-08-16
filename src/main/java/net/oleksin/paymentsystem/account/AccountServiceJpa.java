package net.oleksin.paymentsystem.account;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Profile({"springJpaProfile", "default"})
public class AccountServiceJpa implements AccountService {
  
  private final AccountRepository accountRepository;
  
  public AccountServiceJpa(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }
  
  @Override
  public Account getAccountById(Long id) {
    return accountRepository.getById(id);
  }
}
