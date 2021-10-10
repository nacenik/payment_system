package net.oleksin.paymentsystem.account.jpa;

import net.oleksin.paymentsystem.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
