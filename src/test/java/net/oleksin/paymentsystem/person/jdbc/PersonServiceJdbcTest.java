package net.oleksin.paymentsystem.person.jdbc;

import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.accounttype.AccountType;
import net.oleksin.paymentsystem.person.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceJdbcTest {
  
  
  private static final String NAME = "test";
  private static final Long ID = 1234L;
  
  @Mock
  JdbcTemplate jdbcTemplate;
  
  @Mock
  private SqlRowSet sqlRowSet;
  
  @InjectMocks
  PersonServiceJdbc personServiceJdbc;
  
  @Mock
  private ResultSet resultSet;
  
  @Mock
  private PreparedStatement preparedStatement;
  
  @Captor
  private ArgumentCaptor<RowMapper<Person>> rowMapperArgumentCaptor;
  
  @Captor
  private ArgumentCaptor<PreparedStatementCallback<Person>> preparedStatementCallbackCaptor;
  
  Person person;
  
  @BeforeEach
  void setUp() {
    person = Person.builder()
            .id(1L)
            .firstName(NAME)
            .lastName(NAME)
            .accounts(Set.of(Account.builder().id(1L).build(), Account.builder().id(2L).build()))
            .build();
  }
  
  @Test
  void saveNewPersonTest() throws SQLException {
    personServiceJdbc.saveNewPerson(person);
  
    verify(jdbcTemplate).execute(anyString(), preparedStatementCallbackCaptor.capture());
    PreparedStatementCallback<Person> callback = preparedStatementCallbackCaptor.getValue();
    
    Person savedPerson = callback.doInPreparedStatement(preparedStatement);
    
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
    
    Set<Account> accounts = personServiceJdbc.getAccountsByPersonId(ID);
  
    verify(sqlRowSet).beforeFirst();
    verify(jdbcTemplate).queryForRowSet(anyString(), eq(ID));
  
    assertNotNull(accounts);
    assertEquals(2, accounts.size());
  }
}