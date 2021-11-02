package net.oleksin.paymentsystem.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.oleksin.paymentsystem.Converter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Payment Controller", description = "Get or create operation with payments")
@RestController
@RequestMapping(value = "/api",produces = { MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_XML_VALUE })
@AllArgsConstructor
public class PaymentController {
  
  private final PaymentService paymentService;
  private final BatchPaymentService batchPaymentService;
  private final Converter<PaymentRequestDto, PaymentResponseDto, Payment> paymentConverter;

  @Operation(summary = "Create new payment")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "201",
                  description = "Creating new payment",
                  content = {
                          @Content(
                                  mediaType = "application/json",
                                  array = @ArraySchema(schema = @Schema(implementation = PaymentResponseDto.class))),
                          @Content(
                                  mediaType = "application/xml",
                                  array = @ArraySchema(schema = @Schema(implementation = PaymentResponseDto.class))),
                  })
  })
  @PostMapping(value = "/payment")
  public ResponseEntity<Object> createPayment(@Parameter(description = "New payment") @RequestBody PaymentRequestDto paymentRequestDto) {
    Payment payment = paymentService.createNewPayment(paymentConverter.fromRequestDto(paymentRequestDto));
    return ResponseEntity
            .status(201)
            .body(paymentConverter.toResponseDto(payment));
  }

  @Operation(summary = "Create new payments")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "200",
                  description = "Creating new batch of payments",
                  content = {
                          @Content(
                                  mediaType = "application/json",
                                  array = @ArraySchema(schema = @Schema(implementation = PaymentResponseDto.class))),
                          @Content(
                                  mediaType = "application/xml",
                                  array = @ArraySchema(schema = @Schema(implementation = PaymentResponseDto.class))),
                  })
  })
  @PostMapping(value = "/payments")
  public List<PaymentResponseDto> createPayments(@Parameter(description = "Array of new payments") @RequestBody List<PaymentRequestDto> paymentRequestDtoSet) {
    List<Payment> fromDto = paymentRequestDtoSet.stream()
            .map(paymentConverter::fromRequestDto)
            .collect(Collectors.toList());
    List<Payment> payments = batchPaymentService.createNewPayments(fromDto);
    return payments.stream()
            .map(paymentConverter::toResponseDto)
            .collect(Collectors.toList());
  }

  @Operation(summary = "Get payment")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "200",
                  description = "Getting payment by specific fields",
                  content = {
                          @Content(
                                  mediaType = "application/json",
                                  array = @ArraySchema(schema = @Schema(implementation = PaymentJournalDto.class))),
                          @Content(
                                  mediaType = "application/xml",
                                  array = @ArraySchema(schema = @Schema(implementation = PaymentJournalDto.class))),
                  })
  })
  @GetMapping(value = "/payments")
  public List<PaymentJournalDto> GetPaymentJournal(
          @Parameter(description = "Payer id") @RequestParam(required = false) Long payerId ,
          @Parameter(description = "Recipient id") @RequestParam(required = false) Long recipientId,
          @Parameter(description = "Source account id") @RequestParam(required = false) Long srcAccId,
          @Parameter(description = "Destination account id") @RequestParam(required = false) Long destAccId) {
    List<PaymentJournalDto> paymentJournalDtos = batchPaymentService.getPaymentJournals(payerId, recipientId, srcAccId, destAccId);
    return paymentJournalDtos;
  }
  

}
