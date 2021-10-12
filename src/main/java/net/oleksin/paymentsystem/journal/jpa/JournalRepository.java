package net.oleksin.paymentsystem.journal.jpa;

import net.oleksin.paymentsystem.journal.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalRepository extends JpaRepository<Journal, Long> {
}
