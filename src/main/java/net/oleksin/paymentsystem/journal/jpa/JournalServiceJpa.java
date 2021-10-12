package net.oleksin.paymentsystem.journal.jpa;


import net.oleksin.paymentsystem.journal.Journal;
import net.oleksin.paymentsystem.journal.JournalRequestDto;
import net.oleksin.paymentsystem.journal.JournalSavingDto;
import net.oleksin.paymentsystem.journal.JournalService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Profile({"springJpaProfile", "default"})
public class JournalServiceJpa implements JournalService {
    private final JournalRepository journalRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public JournalServiceJpa(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }


    @Override
    public Journal saveNewRecording(JournalSavingDto journalSavingDto) {
        Journal journal = fromJournalSavingDto(journalSavingDto);
        Journal savedJournal = journalRepository.save(journal);
        return savedJournal;
    }

    @Override
    public List<Journal> findRecording(JournalRequestDto journalRequestDto) {
        return null;
    }

    private Journal fromJournalSavingDto(JournalSavingDto journalSavingDto) {
        return Journal.builder()
                .payment(journalSavingDto.getPayment())
                .payer(journalSavingDto.getPayer())
                .recipient(journalSavingDto.getRecipient())
                .build();
    }
}
