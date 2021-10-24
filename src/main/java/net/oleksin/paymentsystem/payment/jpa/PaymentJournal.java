package net.oleksin.paymentsystem.payment.jpa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
public class PaymentJournal {

    private long id;
    private LocalDateTime timestamp;
    private BigDecimal amount;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private String payerFirstName;
    private String payerLastName;
    private String recipientFirstName;
    private String recipientLastName;

}
