package net.oleksin.paymentsystem.payment.jpa;

import net.oleksin.paymentsystem.payment.PaymentJournalDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentJournalConverterTest {
    private static final String TEST = "test";
    private PaymentJournalConverter paymentJournalConverter;

    @BeforeEach
    void setUp() {
        paymentJournalConverter = new PaymentJournalConverter();
    }

    @Test
    void toResponseDtoTest() {
        PaymentJournal paymentJournal = PaymentJournal.builder()
                .id(1L)
                .amount(new BigDecimal(5))
                .sourceAccountNumber("123123")
                .destinationAccountNumber("321321")
                .timestamp(LocalDateTime.now())
                .payerFirstName(TEST)
                .payerLastName(TEST)
                .recipientFirstName(TEST)
                .recipientLastName(TEST)
                .build();
        PaymentJournalDto paymentJournalDto = paymentJournalConverter.toResponseDto(paymentJournal);

        assertNotNull(paymentJournalDto);
        assertEquals(paymentJournal.getId(), paymentJournalDto.getPaymentId());
        assertEquals(paymentJournal.getAmount(), paymentJournalDto.getAmount());
        assertEquals(paymentJournal.getTimestamp(), paymentJournalDto.getTimestamp());
        assertEquals(paymentJournal.getSourceAccountNumber(), paymentJournalDto.getSourceAccountNumber());
        assertEquals(paymentJournal.getDestinationAccountNumber(), paymentJournalDto.getDestinationAccountNumber());
        assertEquals(paymentJournal.getPayerFirstName(), paymentJournalDto.getPayer().getFirstName());
        assertEquals(paymentJournal.getPayerLastName(), paymentJournalDto.getPayer().getLastName());
        assertEquals(paymentJournal.getRecipientFirstName(), paymentJournalDto.getRecipient().getFirstName());
        assertEquals(paymentJournal.getRecipientLastName(), paymentJournalDto.getRecipient().getLastName());

    }
}