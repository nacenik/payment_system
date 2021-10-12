package net.oleksin.paymentsystem.journal;

import java.util.List;

public interface JournalService {
    Journal saveNewRecording(JournalSavingDto journalSavingDto);
    List<Journal> findRecording(JournalRequestDto journalRequestDto);
}
