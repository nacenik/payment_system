package net.oleksin.paymentsystem.journal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class JournalRequestDto {
    private Long payerId;
    private Long recipientId;
    private Long sourceAccountId;
    private Long destinationAccountId;
}
