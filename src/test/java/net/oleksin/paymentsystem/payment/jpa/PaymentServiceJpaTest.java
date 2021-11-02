package net.oleksin.paymentsystem.payment.jpa;

import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.jpa.AccountRepository;
import net.oleksin.paymentsystem.exception.AccountNotFoundException;
import net.oleksin.paymentsystem.exception.PaymentNotFoundException;
import net.oleksin.paymentsystem.payment.Payment;
import net.oleksin.paymentsystem.payment.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceJpaTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private PaymentServiceJpa paymentServiceJpa;

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder()
                .id(1L)
                .amount(new BigDecimal(500))
                .timestamp(LocalDateTime.now())
                .status(Status.ok)
                .source(Account.builder()
                        .id(1L)
                        .balance(new BigDecimal(1000))
                        .build())
                .destination(Account.builder()
                        .id(2L)
                        .balance(new BigDecimal(1000))
                        .build())
                .reason("test")
                .build();
    }

    @Test
    void createNewPaymentTest() {
        Payment paymentForTest = Payment.builder()
                .source(payment.getSource())
                .destination(payment.getDestination())
                .amount(payment.getAmount())
                .reason(payment.getReason())
                .build();

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(payment.getSource()))
                .thenReturn(Optional.of(payment.getDestination()));
        when(paymentRepository.save(any()))
                .thenReturn(payment);

        Payment expected = paymentServiceJpa.createNewPayment(paymentForTest);

        assertNotNull(expected);
        assertEquals(payment, expected);
        verify(paymentRepository).save(any());
        verify(accountRepository, times(2)).save(any());
    }

    @Test
    void createNewPaymentWhenIncorrectInputTest() {
        Payment paymentForTest = Payment.builder()
                .source(payment.getSource())
                .destination(payment.getDestination())
                .reason(payment.getReason())
                .build();

        Throwable throwable = assertThrows(PaymentNotFoundException.class, () -> paymentServiceJpa.createNewPayment(paymentForTest));

        assertNotNull(throwable);
        assertEquals("Payment doesn't contains valid fields", throwable.getMessage());
    }

    @Test
    void createNewPaymentWhenSourceAccountNotFoundInDbTest() {
        Payment paymentForTest = Payment.builder()
                .source(payment.getSource())
                .destination(payment.getDestination())
                .amount(payment.getAmount())
                .reason(payment.getReason())
                .build();

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(payment.getDestination()));

        Throwable throwable = assertThrows(AccountNotFoundException.class, () -> paymentServiceJpa.createNewPayment(paymentForTest));

        assertNotNull(throwable);
        assertEquals("Source account not found", throwable.getMessage());
    }

    @Test
    void createNewPaymentWhenDestinationAccountNotFoundInDbTest() {
        Payment paymentForTest = Payment.builder()
                .source(payment.getSource())
                .destination(payment.getDestination())
                .amount(payment.getAmount())
                .reason(payment.getReason())
                .build();

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(payment.getSource()))
                .thenReturn(Optional.empty());

        Throwable throwable = assertThrows(AccountNotFoundException.class, () -> paymentServiceJpa.createNewPayment(paymentForTest));

        assertNotNull(throwable);
        assertEquals("Destination account not found", throwable.getMessage());
    }

    @Test
    void createNewPaymentWhenDontHaveEnoughBalanceOnSourceAccountTest() {
        Payment paymentForTest = Payment.builder()
                .source(payment.getSource())
                .destination(payment.getDestination())
                .amount(payment.getAmount())
                .reason(payment.getReason())
                .build();

        payment.getSource().setBalance(new BigDecimal(100));
        payment.setStatus(Status.error);

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(payment.getSource()))
                .thenReturn(Optional.of(payment.getDestination()));
        when(paymentRepository.save(any()))
                .thenReturn(payment);

        Payment expected = paymentServiceJpa.createNewPayment(paymentForTest);

        assertNotNull(expected);
        assertEquals(payment, expected);
    }
}