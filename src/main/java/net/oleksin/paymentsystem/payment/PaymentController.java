package net.oleksin.paymentsystem.payment;

import net.oleksin.paymentsystem.account.Account;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/payment",
        produces = { MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE })
@RestController
public class PaymentController {
  
  private final PaymentService paymentService;
  
  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }
  
  @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE })
  public PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto) {
    return toResponseDto(paymentService.createNewPayment(fromRequestDto(paymentRequestDto)));
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
