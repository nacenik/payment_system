package net.oleksin.paymentsystem.accounttype.jpa;

import lombok.AllArgsConstructor;
import net.oleksin.paymentsystem.accounttype.AccountType;
import net.oleksin.paymentsystem.accounttype.AccountTypeService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"springJpaProfile", "default"})
@AllArgsConstructor
public class AccountTypeServiceJpa implements AccountTypeService {
  private final AccountTypeRepository accountTypeRepository;
  
  @Override
  public AccountType saveNewAccountType(AccountType accountType) {
    return accountTypeRepository.save(accountType);
  }
}
