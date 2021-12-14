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
import org.mockito.stubbing.Answer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountTypeServiceJdbcTest {
  
  @Mock
  private JdbcTemplate jdbcTemplate;
  
  @Mock
  private PreparedStatement preparedStatement;
  
  @InjectMocks
  private AccountTypeServiceJdbc accountTypeServiceJdbc;

  @Mock
  private Connection connection;

  @Mock
  private ResultSet resultSet;

  @Captor
  private ArgumentCaptor<RowMapper<AccountType>> rowMapperArgumentCaptor;

  @Captor
  private ArgumentCaptor<PreparedStatementCreator> preparedStatementCreatorArgumentCaptor;
  
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
    when(connection.prepareStatement(anyString(), any(String[].class)))
            .thenReturn(preparedStatement);

    when(jdbcTemplate.update(preparedStatementCreatorArgumentCaptor.capture(), any(KeyHolder.class))).thenAnswer((Answer) invocation -> {
      Object[] args = invocation.getArguments();
      Map<String, Object> keyMap = new HashMap<>();
      keyMap.put("", 1);
      ((GeneratedKeyHolder)args[1]).getKeyList().add(keyMap);
      return 1;
    }).thenReturn(1);
    when(jdbcTemplate.queryForObject(anyString(), eq(Integer.TYPE), anyString())).thenReturn(0);

    AccountType savedAccountType = accountTypeServiceJdbc.saveNewAccountType(accountType);

    verify(jdbcTemplate).update(preparedStatementCreatorArgumentCaptor.capture(), any(KeyHolder.class));
    PreparedStatementCreator prepared = preparedStatementCreatorArgumentCaptor.getValue();
    prepared.createPreparedStatement(connection);
    verify(connection).prepareStatement(anyString(), any(String[].class));
    verify(preparedStatement).setString(anyInt(), anyString());


    assertNotNull(savedAccountType);
    assertEquals(accountType, savedAccountType);
  }


  @Test
  void saveNewAccountTypeWithExistingTest() throws SQLException {

    when(jdbcTemplate.queryForObject(anyString(), any(Integer.TYPE.getClass()), anyString())).thenReturn(1);
    accountTypeServiceJdbc.saveNewAccountType(accountType);
    verify(jdbcTemplate).queryForObject(anyString(), rowMapperArgumentCaptor.capture(), anyString());

    RowMapper<AccountType> rowMapper = rowMapperArgumentCaptor.getValue();
    when(resultSet.getLong("id"))
            .thenReturn(accountType.getId());
    when(resultSet.getString("name"))
            .thenReturn(accountType.getName());


    AccountType savedAccountType = rowMapper.mapRow(resultSet, anyInt());
    assertNotNull(savedAccountType);
    assertEquals(accountType, savedAccountType);
  }
}