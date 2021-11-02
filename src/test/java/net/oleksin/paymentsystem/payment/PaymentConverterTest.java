package net.oleksin.paymentsystem.payment;

import net.oleksin.paymentsystem.account.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentConverterTest {

    private static final String TEST = "test";

    private PaymentConverter paymentConverter;

    private Payment payment;
    private PaymentResponseDto paymentResponseDto;
    private PaymentRequestDto paymentRequestDto;

    @BeforeEach
    void setUp() {
        paymentConverter = new PaymentConverter();
        payment = Payment.builder()
                .id(1L)
                .amount(new BigDecimal(500))
                .source(Account.builder().id(1L).build())
                .destination(Account.builder().id(2L).build())
                .timestamp(LocalDateTime.now())
                .reason(TEST)
                .status(Status.ok)
                .build();
        paymentResponseDto = PaymentResponseDto.builder()
                .id(payment.getId())
                .status(payment.getStatus())
                .build();
        paymentRequestDto = PaymentRequestDto.builder()
                .sourceAccountId(payment.getSource().getId())
                .destinationAccountId(payment.getDestination().getId())
                .reason(payment.getReason())
                .amount(payment.getAmount())
                .build();
    }

    @Test
    void toResponseDtoTest() {
        PaymentResponseDto expected = paymentConverter.toResponseDto(payment);

        assertNotNull(expected);
        assertEquals(paymentResponseDto, expected);
    }

    @Test
    void fromRequestDtoTest() {
        Payment expected = paymentConverter.fromRequestDto(paymentRequestDto);

        assertNotNull(expected);
        assertEquals(paymentRequestDto.getSourceAccountId(), expected.getSource().getId());
        assertEquals(paymentRequestDto.getDestinationAccountId(), expected.getDestination().getId());
        assertEquals(paymentRequestDto.getReason(), expected.getReason());
        assertEquals(paymentRequestDto.getAmount(), expected.getAmount());
    }
}