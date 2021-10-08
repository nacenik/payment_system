package net.oleksin.paymentsystem.accounttype;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"springJpaProfile", "default"})
public class AccountTypeServiceJpa implements AccountTypeService {
  private final AccountTypeRepository accountTypeRepository;
  
  public AccountTypeServiceJpa(AccountTypeRepository accountTypeRepository) {
    this.accountTypeRepository = accountTypeRepository;
  }
  
  @Override
  public AccountType saveNewAccountType(AccountType accountType) {
    return accountTypeRepository.save(accountType);
  }
}
