package net.oleksin.paymentsystem.payment;

import net.oleksin.paymentsystem.Converter;
import net.oleksin.paymentsystem.account.Account;
import org.springframework.stereotype.Component;

@Component
public class PaymentConverter implements Converter<PaymentRequestDto, PaymentResponseDto, Payment> {
  
  @Override
  public PaymentResponseDto toResponseDto(Payment payment) {
    return PaymentResponseDto.builder()
            .id(payment.getId())
            .status(payment.getStatus())
            .build();
  }
  
  @Override
  public Payment fromRequestDto(PaymentRequestDto paymentRequestDto) {
    return Payment.builder()
            .source(Account.builder().id(paymentRequestDto.getSourceAccountId()).build())
            .destination(Account.builder().id(paymentRequestDto.getDestinationAccountId()).build())
            .amount(paymentRequestDto.getAmount())
            .reason(paymentRequestDto.getReason())
            .build();
  }
}
