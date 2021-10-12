package net.oleksin.paymentsystem.payment;

import net.oleksin.paymentsystem.account.Account;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_XML_VALUE })
@RestController
public class PaymentController {
  
  private final PaymentService paymentService;
  private final BatchPaymentService batchPaymentService;
  
  public PaymentController(PaymentService paymentService, BatchPaymentService batchPaymentService) {
    this.paymentService = paymentService;
    this.batchPaymentService = batchPaymentService;
  }
  
  @PostMapping(value = "/payment",
          consumes = { MediaType.APPLICATION_JSON_VALUE,
                  MediaType.APPLICATION_XML_VALUE })
  public PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto) {
    return toResponseDto(paymentService.createNewPayment(fromRequestDto(paymentRequestDto)));
  }

  @PostMapping(value = "/payments",
          consumes = { MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE })
  public Set<PaymentResponseDto> createPayment(Set<Payment> payments) {
    return toResponseDto(batchPaymentService.createNewPayments(payments));
  }

  private Set<PaymentResponseDto> toResponseDto(Set<Payment> payments) {
    return payments
            .stream()
            .map(payment -> PaymentResponseDto.builder()
                    .id(payment.getId())
                    .status(payment.getStatus())
                    .build())
            .collect(Collectors.toSet());
  }
  
  private PaymentResponseDto toResponseDto(Payment payment) {
    return PaymentResponseDto.builder().id(payment.getId()).build();
  }
  
  private Payment fromRequestDto(PaymentRequestDto paymentRequestDto) {
    return Payment.builder()
            .source(fromRequestDto(paymentRequestDto.getSourceAccountId()))
            .destination(fromRequestDto(paymentRequestDto.getSourceAccountId()))
            .amount(paymentRequestDto.getAmount())
            .reason(paymentRequestDto.getReason())
            .build();
  }
  
  private Account fromRequestDto(Long id) {
    return Account.builder()
            .id(id)
            .build();
  }

}
