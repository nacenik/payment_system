package net.oleksin.paymentsystem.accounttype.jdbc;

import net.oleksin.paymentsystem.accounttype.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountTypeServiceJdbcTest {
  
  @Mock
  private JdbcTemplate jdbcTemplate;
  
  @Mock
  private PreparedStatement preparedStatement;
  
  @InjectMocks
  private AccountTypeServiceJdbc accountTypeServiceJdbc;
  
  @Captor
  private ArgumentCaptor<PreparedStatementCallback<AccountType>> argumentCaptor;
  
  private AccountType accountType;
  
  @BeforeEach
  void setUp() {
    accountType = AccountType.builder()
            .id(1L)
            .name("simple")
            .build();
  }
  
  @Test
  void saveNewAccountTypeTest() throws SQLException {
    accountTypeServiceJdbc.saveNewAccountType(accountType);
  
    verify(jdbcTemplate).execute(anyString(), argumentCaptor.capture());
    PreparedStatementCallback<AccountType> callback = argumentCaptor.getValue();
  
    AccountType savedAccountType = callback.doInPreparedStatement(preparedStatement);
  
    assertNotNull(savedAccountType);
    assertEquals(accountType, savedAccountType);
  }
}