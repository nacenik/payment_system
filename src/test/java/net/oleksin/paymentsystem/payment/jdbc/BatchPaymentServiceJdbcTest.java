package net.oleksin.paymentsystem.payment.jdbc;

import net.oleksin.paymentsystem.payment.Payment;
import net.oleksin.paymentsystem.payment.PaymentJournalDto;
import net.oleksin.paymentsystem.payment.PaymentService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatchPaymentServiceJdbcTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Mock
    private SqlRowSet sqlRowSet;

    @InjectMocks
    private BatchPaymentServiceJdbc batchPaymentServiceJdbc;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createNewPaymentsTest() {
        Payment payment1 = Payment.builder().id(1L).build();
        Payment payment2 = Payment.builder().id(2L).build();

        when(paymentService.createNewPayment(any(Payment.class)))
                .thenReturn(payment1)
                .thenReturn(payment2);
        List<Payment> payments = batchPaymentServiceJdbc.createNewPayments(List.of(payment1, payment2));

        verify(paymentService, times(2)).createNewPayment(any(Payment.class));
        assertNotNull(payments);
        assertEquals(2, payments.size());
    }

    @Test
    void createNewPaymentsWithEmptyListTest() {
        List<Payment> payments = batchPaymentServiceJdbc.createNewPayments(Lists.emptyList());

        assertNotNull(payments);
        assertEquals(0, payments.size());
    }

    @Test
    void getPaymentJournalsWithAllParametersTest() {
        String test = "TEST";
        LocalDateTime localDateTime = LocalDateTime.now();
        BigDecimal bigDecimal = new BigDecimal(500);

        when(jdbcTemplate.queryForRowSet(anyString(), any(Map.class)))
                .thenReturn(sqlRowSet);
        when(sqlRowSet.next()).thenReturn(true).thenReturn(false);
        when(sqlRowSet.getLong("id")).thenReturn(1L);
        when(sqlRowSet.getTimestamp("timestamp")).thenReturn(Timestamp.valueOf(localDateTime));
        when(sqlRowSet.getString("source_account_number")).thenReturn(test);
        when(sqlRowSet.getString("destination_account_number")).thenReturn(test);
        when(sqlRowSet.getBigDecimal("amount")).thenReturn(bigDecimal);
        when(sqlRowSet.getString("payer_first_name")).thenReturn(test);
        when(sqlRowSet.getString("payer_last_name")).thenReturn(test);
        when(sqlRowSet.getString("recipient_first_name")).thenReturn(test);
        when(sqlRowSet.getString("recipient_last_name")).thenReturn(test);

        List<PaymentJournalDto> paymentJournalDtos = batchPaymentServiceJdbc.getPaymentJournals(1L,2L,3L,4L);

        verify(sqlRowSet).beforeFirst();
        assertNotNull(paymentJournalDtos);
        assertEquals(1, paymentJournalDtos.size());
        verifyPaymentJournal(test, 1L, bigDecimal, localDateTime, paymentJournalDtos.get(0));
    }

    @Test
    void getPaymentJournalsWithoutParametersTest() {
        String firstTestString = "TEST1";
        LocalDateTime firstTime = LocalDateTime.now();
        BigDecimal firstAmount = new BigDecimal(500);

        String secondTestString = "TEST2";
        LocalDateTime secondTime = LocalDateTime.now();
        BigDecimal secondAmount = new BigDecimal(999);

        when(jdbcTemplate.queryForRowSet(anyString(), any(Map.class)))
                .thenReturn(sqlRowSet);
        when(sqlRowSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(sqlRowSet.getLong("id")).thenReturn(1L).thenReturn(2L);
        when(sqlRowSet.getTimestamp("timestamp")).thenReturn(Timestamp.valueOf(firstTime)).thenReturn(Timestamp.valueOf(secondTime));
        when(sqlRowSet.getString("source_account_number")).thenReturn(firstTestString).thenReturn(secondTestString);
        when(sqlRowSet.getString("destination_account_number")).thenReturn(firstTestString).thenReturn(secondTestString);
        when(sqlRowSet.getBigDecimal("amount")).thenReturn(firstAmount).thenReturn(secondAmount);
        when(sqlRowSet.getString("payer_first_name")).thenReturn(firstTestString).thenReturn(secondTestString);
        when(sqlRowSet.getString("payer_last_name")).thenReturn(firstTestString).thenReturn(secondTestString);
        when(sqlRowSet.getString("recipient_first_name")).thenReturn(firstTestString).thenReturn(secondTestString);
        when(sqlRowSet.getString("recipient_last_name")).thenReturn(firstTestString).thenReturn(secondTestString);

        List<PaymentJournalDto> paymentJournalDtos = batchPaymentServiceJdbc.getPaymentJournals(null,null,null,null);

        verify(sqlRowSet).beforeFirst();
        assertNotNull(paymentJournalDtos);
        assertEquals(2, paymentJournalDtos.size());
        verifyPaymentJournal(firstTestString, 1L, firstAmount, firstTime, paymentJournalDtos.get(0));
        verifyPaymentJournal(secondTestString, 2L, secondAmount, secondTime, paymentJournalDtos.get(1));
    }

    private void verifyPaymentJournal(String testString, Long id, BigDecimal amount, LocalDateTime localDateTime, PaymentJournalDto paymentJournalDto) {
        assertEquals(id, paymentJournalDto.getPaymentId());
        assertEquals(testString, paymentJournalDto.getSourceAccountNumber());
        assertEquals(testString, paymentJournalDto.getDestinationAccountNumber());
        assertEquals(testString, paymentJournalDto.getPayer().getFirstName());
        assertEquals(testString, paymentJournalDto.getPayer().getLastName());
        assertEquals(testString, paymentJournalDto.getRecipient().getFirstName());
        assertEquals(testString, paymentJournalDto.getRecipient().getLastName());
        assertEquals(amount, paymentJournalDto.getAmount());
        assertEquals(localDateTime, paymentJournalDto.getTimestamp());
    }
}