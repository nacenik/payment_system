package net.oleksin.paymentsystem.person.jdbc;

import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountService;
import net.oleksin.paymentsystem.person.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceJdbcTest {
  
  
  private static final String NAME = "test";
  private static final Long ID = 1234L;
  
  @Mock
  private JdbcTemplate jdbcTemplate;
  
  @Mock
  private SqlRowSet sqlRowSet;

  @Mock
  private AccountService accountService;
  
  @InjectMocks
  private PersonServiceJdbc personServiceJdbc;

  @Mock
  private Connection connection;

  @Mock
  private ResultSet resultSet;
  
  @Mock
  private PreparedStatement preparedStatement;
  
  @Captor
  private ArgumentCaptor<RowMapper<Person>> rowMapperArgumentCaptor;
  
  @Captor
  private ArgumentCaptor<PreparedStatementCallback<Person>> preparedStatementCallbackCaptor;

  @Captor
  private ArgumentCaptor<PreparedStatementCreator> preparedStatementCreatorArgumentCaptor;

  private Account firstAccount;
  private Account secondAccount;
  private Person person;
  
  @BeforeEach
  void setUp() {
    firstAccount = Account.builder().id(1L).build();
    secondAccount = Account.builder().id(2L).build();
    person = Person.builder()
            .id(1L)
            .firstName(NAME)
            .lastName(NAME)
            .accounts(List.of(firstAccount, secondAccount))
            .build();
  }
  
  @Test
  void saveNewPersonTest() throws SQLException {
    when(connection.prepareStatement(anyString(), any(String[].class)))
            .thenReturn(preparedStatement);

    when(accountService.saveNewAccount(any(Account.class)))
            .thenReturn(firstAccount)
            .thenReturn(secondAccount);

    when(jdbcTemplate.update(preparedStatementCreatorArgumentCaptor.capture(), any(KeyHolder.class)))
            .thenAnswer((Answer) invocation -> {
      Object[] args = invocation.getArguments();
      Map<String, Object> keyMap = new HashMap<>();
      keyMap.put("", 1);
      ((GeneratedKeyHolder)args[1]).getKeyList().add(keyMap);
      return 1;
    }).thenReturn(1);
    Person savedPerson = personServiceJdbc.saveNewPerson(person);

    verify(jdbcTemplate).update(preparedStatementCreatorArgumentCaptor.capture(), any(KeyHolder.class));

    PreparedStatementCreator prepared = preparedStatementCreatorArgumentCaptor.getValue();
    prepared.createPreparedStatement(connection);

    verify(connection).prepareStatement(anyString(), any(String[].class));
    verify(preparedStatement, times(2)).setString(anyInt(), anyString());

    assertNotNull(savedPerson);
    assertEquals(person, savedPerson);
  }
  
  @Test
  void getAllPersonsTest() {
    List<Person> personList = List.of(person, Person.builder().id(2L).build());
    when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class)))
            .thenReturn(personList);
  
    List<Person> expectedList = personServiceJdbc.getAllPersons();
  
    assertNotNull(expectedList);
    assertFalse(expectedList.isEmpty());
    assertEquals(2, expectedList.size());
    assertEquals(personList, expectedList);
  
    verify(jdbcTemplate, times(1))
            .query(anyString(), any(BeanPropertyRowMapper.class));
  }
  
  @Test
  void getPersonByIdTest() throws SQLException {
    personServiceJdbc.getPersonById(ID);
  
    verify(jdbcTemplate).queryForObject(anyString(), rowMapperArgumentCaptor.capture(), eq(ID));
    RowMapper<Person> rowMapper = rowMapperArgumentCaptor.getValue();
    when(resultSet.getLong(1))
            .thenReturn(person.getId());
    when(resultSet.getString(2))
            .thenReturn(person.getFirstName());
    when(resultSet.getString(3))
            .thenReturn(person.getLastName());
    
    
    Person expectedPerson = rowMapper.mapRow(resultSet, anyInt());
    assertNotNull(expectedPerson);
    assertEquals(person.getId(), expectedPerson.getId());
    assertEquals(person.getLastName(), expectedPerson.getLastName());
    assertEquals(person.getId(), expectedPerson.getId());
  }
  
  @Test
  void getAccountsByPersonId() throws SQLException {
    when(jdbcTemplate.queryForRowSet(anyString(), eq(ID)))
            .thenReturn(sqlRowSet);
    when(sqlRowSet.next())
            .thenReturn(true, true, false);
    when(sqlRowSet.getLong(6))
            .thenReturn(ID, ID);
    when(sqlRowSet.getString(7))
            .thenReturn(NAME, NAME);
    when(sqlRowSet.getLong(1))
            .thenReturn(ID, ID);
    when(sqlRowSet.getString(2))
            .thenReturn(NAME, NAME);
    when(sqlRowSet.getBigDecimal(3))
            .thenReturn(new BigDecimal(ID), new BigDecimal(ID));
    
    List<Account> accounts = personServiceJdbc.getAccountsByPersonId(ID);
  
    verify(sqlRowSet).beforeFirst();
    verify(jdbcTemplate).queryForRowSet(anyString(), eq(ID));
  
    assertNotNull(accounts);
    assertEquals(2, accounts.size());
  }
}