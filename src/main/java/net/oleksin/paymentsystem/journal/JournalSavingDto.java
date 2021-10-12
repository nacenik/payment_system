package net.oleksin.paymentsystem.journal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.oleksin.paymentsystem.payment.Payment;
import net.oleksin.paymentsystem.person.Person;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class JournalSavingDto {
    private Payment payment;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private Person payer;
    private Person recipient;
}
