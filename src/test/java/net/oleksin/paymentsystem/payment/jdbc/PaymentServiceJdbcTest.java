package net.oleksin.paymentsystem.payment.jdbc;

import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.accounttype.AccountType;
import net.oleksin.paymentsystem.exception.AccountNotFoundException;
import net.oleksin.paymentsystem.exception.PaymentNotFoundException;
import net.oleksin.paymentsystem.payment.Payment;
import net.oleksin.paymentsystem.payment.Status;
import net.oleksin.paymentsystem.person.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceJdbcTest {

    private static final String TEST = "test";

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private GeneratedKeyHolder keyHolder;

    @Mock
    private ResultSet resultSet;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Connection connection;

    @InjectMocks
    private PaymentServiceJdbc paymentServiceJdbc;

    @Captor
    private ArgumentCaptor<ResultSetExtractor<Account>> rseCaptor;

    @Captor
    private ArgumentCaptor<PreparedStatementCreator> preparedStatementCreatorArgumentCaptor;

    private Payment payment;
    private Account sourceAccount;
    private Account destinationAccount;

    @BeforeEach
    void setUp() {
        Person person = Person.builder()
                .id(1L)
                .firstName(TEST)
                .lastName(TEST)
                .build();
        sourceAccount = Account.builder()
                .id(1L)
                .balance(new BigDecimal(1000))
                .accountNumber("1234")
                .accountType(AccountType.builder()
                        .id(1L)
                        .name(TEST)
                        .build())
                .person(person)
                .build();

        destinationAccount = Account.builder()
                .id(2L)
                .balance(new BigDecimal(500))
                .accountNumber("1234")
                .accountType(AccountType.builder()
                        .id(1L)
                        .name(TEST)
                        .build())
                .person(person)
                .build();

        payment = Payment.builder()
                .id(1L)
                .amount(new BigDecimal(500))
                .timestamp(LocalDateTime.now())
                .status(Status.ok)
                .source(sourceAccount)
                .destination(destinationAccount)
                .reason("test")
                .build();
    }

    @Test
    void createNewPaymentTest() throws SQLException {
        when(jdbcTemplate.update(anyString(), any(), anyLong())).thenReturn(1).thenReturn(1);
        Payment newPayment = verifyCreatingNewPaymentAndGetIt();
        verify(jdbcTemplate, times(2)).update(anyString(), any(), anyLong());
        assertEquals(Status.ok, newPayment.getStatus());
    }

    @Test
    void createNewPaymentWithStatusErrorTest() throws SQLException {
        sourceAccount.setBalance(new BigDecimal(0));
        Payment newPayment = verifyCreatingNewPaymentAndGetIt();
        assertEquals(Status.error, newPayment.getStatus());
    }


    @Test
    void createNewPaymentWithRequestParametersTest() throws SQLException {
        Payment paymentForRequest = Payment.builder()
                .source(Account.builder().id(sourceAccount.getId()).build())
                .destination(Account.builder().id(destinationAccount.getId()).build())
                .build();

        Throwable throwable = assertThrows(PaymentNotFoundException.class, () -> paymentServiceJdbc.createNewPayment(paymentForRequest));

        assertNotNull(throwable);
        assertEquals("Payment doesn't contains valid fields", throwable.getMessage());
    }

    @Test
    void createNewPaymentWithBadSourceIdTest() throws SQLException {
        Payment paymentForRequest = Payment.builder()
                .reason(payment.getReason())
                .amount(payment.getAmount())
                .source(Account.builder().id(sourceAccount.getId()).build())
                .destination(Account.builder().id(destinationAccount.getId()).build())
                .build();
        when(jdbcTemplate.query(anyString(), rseCaptor.capture(), anyLong()))
                .thenReturn(null);

        Throwable throwable = assertThrows(AccountNotFoundException.class, () -> paymentServiceJdbc.createNewPayment(paymentForRequest));

        assertNotNull(throwable);
        assertEquals("Source account not found", throwable.getMessage());
    }

    @Test
    void createNewPaymentWithBadDestinationIdTest() throws SQLException {
        Payment paymentForRequest = Payment.builder()
                .reason(payment.getReason())
                .amount(payment.getAmount())
                .source(Account.builder().id(sourceAccount.getId()).build())
                .destination(Account.builder().id(destinationAccount.getId()).build())
                .build();
        when(jdbcTemplate.query(anyString(), rseCaptor.capture(), anyLong()))
                .thenReturn(sourceAccount)
                .thenReturn(null);

        Throwable throwable = assertThrows(AccountNotFoundException.class, () -> paymentServiceJdbc.createNewPayment(paymentForRequest));

        when(resultSet.next()).thenReturn(true).thenReturn(false);

        setResultSetForSourceAccount(resultSet);
        ResultSetExtractor<Account> resultSetExtractor = rseCaptor.getValue();
        Account src = resultSetExtractor.extractData(resultSet);

        assertEquals(sourceAccount, src);
        assertNotNull(throwable);
        assertEquals("Destination account not found", throwable.getMessage());
    }

    private Payment verifyCreatingNewPaymentAndGetIt() throws SQLException {
        Payment paymentForRequest = Payment.builder()
                .reason(payment.getReason())
                .amount(payment.getAmount())
                .source(Account.builder().id(sourceAccount.getId()).build())
                .destination(Account.builder().id(destinationAccount.getId()).build())
                .build();
        when(jdbcTemplate.query(anyString(), rseCaptor.capture(), anyLong()))
                .thenReturn(sourceAccount)
                .thenReturn(destinationAccount);
        when(jdbcTemplate.update(preparedStatementCreatorArgumentCaptor.capture(), any(KeyHolder.class))).thenAnswer((Answer) invocation -> {
            Object[] args = invocation.getArguments();
            Map<String, Object> keyMap = new HashMap<>();
            keyMap.put("", 1);
            ((GeneratedKeyHolder)args[1]).getKeyList().add(keyMap);
            return 1;
        }).thenReturn(1);

        when(connection.prepareStatement(anyString(), any(String[].class)))
                .thenReturn(preparedStatement);

        Payment newPayment = paymentServiceJdbc.createNewPayment(paymentForRequest);

        when(resultSet.next()).thenReturn(true).thenReturn(true);
        setResultSetForBothAccounts(resultSet);
        ResultSetExtractor<Account> resultSetExtractor = rseCaptor.getValue();

        Account src = resultSetExtractor.extractData(resultSet);
        Account dest = resultSetExtractor.extractData(resultSet);

        PreparedStatementCreator prepared = preparedStatementCreatorArgumentCaptor.getValue();
        prepared.createPreparedStatement(connection);

        verify(connection).prepareStatement(anyString(), any(String[].class));
        verify(preparedStatement).setBigDecimal(anyInt(), any(BigDecimal.class));
        verify(preparedStatement, times(2)).setString(anyInt(), anyString());
        verify(preparedStatement).setTimestamp(anyInt(), any(Timestamp.class));
        verify(preparedStatement, times(2)).setLong(anyInt(), anyLong());
        verify(jdbcTemplate, times(2)).query(anyString(), rseCaptor.capture(), anyLong());
        verify(jdbcTemplate).update(preparedStatementCreatorArgumentCaptor.capture(), any(KeyHolder.class));

        assertEquals(sourceAccount, src);
        assertEquals(destinationAccount, dest);
        assertNotNull(newPayment);
        assertEquals(sourceAccount, newPayment.getSource());
        assertEquals(destinationAccount, newPayment.getDestination());
        assertNotNull(newPayment.getTimestamp());
        assertEquals(paymentForRequest.getReason(), newPayment.getReason());
        assertEquals(paymentForRequest.getAmount(), newPayment.getAmount());
        assertEquals(1L, newPayment.getId());
        return newPayment;
    }

    private void setResultSetForSourceAccount(ResultSet resultSet) throws SQLException {
        when(resultSet.getLong(1)).thenReturn(sourceAccount.getId());
        when(resultSet.getString("account_number")).thenReturn(sourceAccount.getAccountNumber());
        when(resultSet.getBigDecimal("balance")).thenReturn(sourceAccount.getBalance());
        when(resultSet.getLong("type_id")).thenReturn(sourceAccount.getAccountType().getId());
        when(resultSet.getString("name")).thenReturn(sourceAccount.getAccountType().getName());
        when(resultSet.getLong("person_id")).thenReturn(sourceAccount.getPerson().getId());
        when(resultSet.getString("first_name")).thenReturn(sourceAccount.getPerson().getFirstName());
        when(resultSet.getString("last_name")).thenReturn(sourceAccount.getPerson().getLastName());
    }

    private void setResultSetForBothAccounts(ResultSet resultSet) throws SQLException {
        when(resultSet.getLong(1)).thenReturn(sourceAccount.getId()).thenReturn(destinationAccount.getId());
        when(resultSet.getString("account_number")).thenReturn(sourceAccount.getAccountNumber()).thenReturn(destinationAccount.getAccountNumber());
        when(resultSet.getBigDecimal("balance")).thenReturn(sourceAccount.getBalance()).thenReturn(destinationAccount.getBalance());
        when(resultSet.getLong("type_id")).thenReturn(sourceAccount.getAccountType().getId()).thenReturn(destinationAccount.getAccountType().getId());
        when(resultSet.getString("name")).thenReturn(sourceAccount.getAccountType().getName()).thenReturn(destinationAccount.getAccountType().getName());
        when(resultSet.getLong("person_id")).thenReturn(sourceAccount.getPerson().getId()).thenReturn(destinationAccount.getPerson().getId());
        when(resultSet.getString("first_name")).thenReturn(sourceAccount.getPerson().getFirstName()).thenReturn(destinationAccount.getPerson().getFirstName());
        when(resultSet.getString("last_name")).thenReturn(sourceAccount.getPerson().getLastName()).thenReturn(destinationAccount.getPerson().getLastName());
    }
}