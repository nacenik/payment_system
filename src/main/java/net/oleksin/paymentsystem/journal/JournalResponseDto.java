package net.oleksin.paymentsystem.journal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.oleksin.paymentsystem.person.PersonResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class JournalResponseDto {

    private Long paymentId;
    private LocalDateTime timeStamp;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private BigDecimal amount;
    private PersonResponseDto payer;
    private PersonResponseDto recipient;
}
