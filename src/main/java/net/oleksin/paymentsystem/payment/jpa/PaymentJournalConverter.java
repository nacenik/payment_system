package net.oleksin.paymentsystem.payment.jpa;

import net.oleksin.paymentsystem.ToResponseConverter;
import net.oleksin.paymentsystem.payment.PaymentJournalDto;
import net.oleksin.paymentsystem.person.PersonResponseDto;
import org.springframework.stereotype.Component;

@Component
public class PaymentJournalConverter implements ToResponseConverter<PaymentJournalDto, PaymentJournal> {
    @Override
    public PaymentJournalDto toResponseDto(PaymentJournal paymentJournal) {
        return PaymentJournalDto.builder()
                .paymentId(paymentJournal.getId())
                .timestamp(paymentJournal.getTimestamp())
                .amount(paymentJournal.getAmount())
                .sourceAccountNumber(paymentJournal.getSourceAccountNumber())
                .destinationAccountNumber(paymentJournal.getDestinationAccountNumber())
                .payer(PersonResponseDto.builder()
                        .firstName(paymentJournal.getPayerFirstName())
                        .lastName(paymentJournal.getPayerLastName()).build())
                .recipient(PersonResponseDto.builder()
                        .firstName(paymentJournal.getRecipientFirstName())
                        .lastName(paymentJournal.getRecipientLastName())
                        .build())
                .build();
    }
}
