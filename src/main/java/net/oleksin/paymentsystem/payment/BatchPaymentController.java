package net.oleksin.paymentsystem.payment;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping(value = "/payments",
        produces = { MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE })
@RestController
public class BatchPaymentController {
  
  private final BatchPaymentService batchPaymentService;
  
  public BatchPaymentController(BatchPaymentService batchPaymentService) {
    this.batchPaymentService = batchPaymentService;
  }
  
  @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE,
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
  
  
}
