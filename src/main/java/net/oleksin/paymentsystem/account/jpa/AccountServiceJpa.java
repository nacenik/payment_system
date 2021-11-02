package net.oleksin.paymentsystem.account.jpa;

import lombok.AllArgsConstructor;
import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountService;
import net.oleksin.paymentsystem.exception.AccountNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile({"springJpaProfile", "default"})
@AllArgsConstructor
public class AccountServiceJpa implements AccountService {
  
  private final AccountRepository accountRepository;

  @Transactional
  @Override
  public Account getAccountById(Long id) {
    return accountRepository.findById(id)
            .orElseThrow(AccountNotFoundException::new);
  }

}
