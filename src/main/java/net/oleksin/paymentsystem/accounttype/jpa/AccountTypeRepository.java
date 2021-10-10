package net.oleksin.paymentsystem.accounttype.jpa;

import net.oleksin.paymentsystem.accounttype.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {
}
