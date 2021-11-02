package net.oleksin.paymentsystem.payment.jpa;

import net.oleksin.paymentsystem.ToResponseConverter;
import net.oleksin.paymentsystem.payment.Payment;
import net.oleksin.paymentsystem.payment.PaymentJournalDto;
import net.oleksin.paymentsystem.payment.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BatchPaymentServiceJpaTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private ToResponseConverter<PaymentJournalDto, PaymentJournal> toResponseConverter;

    @InjectMocks
    private BatchPaymentServiceJpa batchPaymentServiceJpa;


    @Test
    void createNewPayments() {
        Payment first = getPaymentForTest(1L);
        Payment second = getPaymentForTest(2L);
        List<Payment> payments = List.of(first, second);

        when(paymentService.createNewPayment(any()))
                .thenReturn(first)
                .thenReturn(second);

        List<Payment> expected = batchPaymentServiceJpa.createNewPayments(payments);

        assertNotNull(expected);
        assertEquals(payments.size(), expected.size());
        assertEquals(payments, expected);
    }

    private Payment getPaymentForTest(Long id) {
        return Payment.builder().id(id).build();
    }
}