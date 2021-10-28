package net.oleksin.paymentsystem.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import net.oleksin.paymentsystem.Converter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Payment Controller", description = "Get or create operation with payments")
@RestController
@RequestMapping(value = "/api",produces = { MediaType.APPLICATION_JSON_VALUE,
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

  @Operation(summary = "Create new payment",
          description = "For creating new payment")
  @PostMapping(value = "/payment")
  public PaymentResponseDto createPayment(@Parameter(description = "New payment") @RequestBody PaymentRequestDto paymentRequestDto) {
    Payment payment = paymentService.createNewPayment(paymentConverter.fromRequestDto(paymentRequestDto));
    return paymentConverter.toResponseDto(payment);
  }

  @Operation(summary = "Create new payments",
          description = "For creating new batch of payments")
  @PostMapping(value = "/payments")
  public List<PaymentResponseDto> createPayment(@Parameter(description = "Array of new payments") @RequestBody List<PaymentRequestDto> paymentRequestDtoSet) {
    List<Payment> fromDto = paymentRequestDtoSet.stream()
            .map(paymentConverter::fromRequestDto)
            .collect(Collectors.toList());
    List<Payment> payments = batchPaymentService.createNewPayments(fromDto);
    return payments.stream()
            .map(paymentConverter::toResponseDto)
            .collect(Collectors.toList());
  }

  @Operation(summary = "Get payment",
          description = "For getting payment by specific fields")
  @GetMapping(value = "/payments")
  public List<PaymentJournalDto> GetPaymentJournal(
          @Parameter(description = "Payer id") @RequestParam(required = false) Long payerId ,
          @Parameter(description = "Recipient id") @RequestParam(required = false) Long recipientId,
          @Parameter(description = "Source account id") @RequestParam(required = false) Long srcAccId,
          @Parameter(description = "Destination account id") @RequestParam(required = false) Long destAccId) {
    //todo delete it in the future(USING ONLY FOR MANUAL TESTING)
    if (payerId == null && recipientId == null && srcAccId == null && destAccId == null) {
      payerId = 1L;
    }
    List<PaymentJournalDto> paymentJournalDtos = batchPaymentService.getPaymentJournals(payerId, recipientId, srcAccId, destAccId);
    return paymentJournalDtos;
  }
  

}
