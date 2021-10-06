package net.oleksin.paymentsystem.account;

import net.oleksin.paymentsystem.person.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Clock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceJdbcTest {

    private static final Long ID = 1234L;

    @InjectMocks
    private AccountServiceJdbc accountServiceJdbc;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ResultSet resultSet;

    @Captor
    private ArgumentCaptor<RowMapper<Account>> rowMapperArgumentCaptor;

    @Test
    void getAccountById() throws SQLException {
        accountServiceJdbc.getAccountById(ID);

        verify(jdbcTemplate).queryForObject(anyString(), rowMapperArgumentCaptor.capture(), eq(ID));
        RowMapper<Account> rowMapper = rowMapperArgumentCaptor.getValue();
        BigDecimal amount = new BigDecimal(ID);

        when(resultSet.getLong(1))
                .thenReturn(ID);
        when(resultSet.getString(2))
                .thenReturn("any");
        when(resultSet.getString(3))
                .thenReturn(AccountType.CREDIT_CARD.getAccountType());
        when(resultSet.getBigDecimal(4))
                .thenReturn(amount);
        when(resultSet.getObject(5))
                .thenReturn(new Person());


        Account account = rowMapper.mapRow(resultSet, anyInt());
        assertNotNull(account);
        assertEquals(ID, account.getId());
        assertEquals(AccountType.CREDIT_CARD, account.getAccountType());
        assertEquals(amount, account.getBalance());
    }
}