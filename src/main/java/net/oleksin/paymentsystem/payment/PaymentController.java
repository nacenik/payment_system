package net.oleksin.paymentsystem.payment;

import lombok.Value;
import net.oleksin.paymentsystem.Converter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_XML_VALUE })
public class PaymentController {
  
  private final PaymentService paymentService;
  private final BatchPaymentService batchPaymentService;
  private final Converter<PaymentRequestDto, PaymentResponseDto, Payment> paymentConverter;
  
  public PaymentController(PaymentService paymentService, BatchPaymentService batchPaymentService, Converter<PaymentRequestDto, PaymentResponseDto, Payment> paymentConverter) {
    this.paymentService = paymentService;
    this.batchPaymentService = batchPaymentService;
    this.paymentConverter = paymentConverter;
  }
  
  @PostMapping(value = "/payment")
  public PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto) {
    Payment payment = paymentService.createNewPayment(paymentConverter.fromRequestDto(paymentRequestDto));
    return paymentConverter.toResponseDto(payment);
  }

  @PostMapping(value = "/payments")
  public List<PaymentResponseDto> createPayment(List<PaymentRequestDto> paymentRequestDtoSet) {
    List<Payment> payments = batchPaymentService.createNewPayments(paymentRequestDtoSet.stream()
            .map(paymentConverter::fromRequestDto)
            .collect(Collectors.toList()));
    return payments.stream()
            .map(paymentConverter::toResponseDto)
            .collect(Collectors.toList());
  }
  
  @GetMapping(value = "/payments")
  public List<PaymentJournalDto> GetPaymentJournal(Long payerId, Long recipientId, Long srcAccId, Long destAccId) {
    //todo delete it in the future(USING ONLY FOR MANUAL TESTING)
    if (payerId == null && recipientId == null && srcAccId == null && destAccId == null) {
      payerId = 1L;
      recipientId = 1L;
      srcAccId = 2L;
      destAccId = 3L;
    }
    List<PaymentJournalDto> paymentJournalDtos = batchPaymentService.getPaymentJournals(payerId, recipientId, srcAccId, destAccId);
    return paymentJournalDtos;
  }
  

}
