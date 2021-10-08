package net.oleksin.paymentsystem.account.springdatajpa;

import net.oleksin.paymentsystem.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
