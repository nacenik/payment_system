package net.oleksin.paymentsystem.person.jpa;

import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.exception.PersonNotFoundException;
import net.oleksin.paymentsystem.person.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceJpaTest {
  
  final String NAME = "test";
  
  @Mock
  private PersonRepository personRepository;
  
  @InjectMocks
  private PersonServiceJpa personServiceJpa;
  
  private Person person;
  
  
  @BeforeEach
  void setUp() {
    person = Person.builder()
            .id(1L)
            .firstName(NAME)
            .lastName(NAME)
            .accounts(List.of(Account.builder().id(1L).build(), Account.builder().id(2L).build()))
            .build();
  }
  
  @Test
  void saveNewPersonTest() {
    when(personRepository.save(any())).thenReturn(person);
    
    Person savedPerson = personServiceJpa.saveNewPerson(person);
    assertNotNull(savedPerson);
    assertEquals(person, savedPerson);
    
    verify(personRepository, times(1)).save(any());
    
  }
  
  @Test
  void getAllPersonsTest() {
    List<Person> personList = new ArrayList<>();
    personList.add(Person.builder().id(1L).build());
    personList.add(Person.builder().id(2L).build());
    when(personRepository.findAll()).thenReturn(personList);
    
    List<Person> allPersons = personServiceJpa.getAllPersons();
    assertNotNull(allPersons);
    assertEquals(personList, allPersons);
    assertEquals(2, allPersons.size());
    
    verify(personRepository, times(1)).findAll();
  }
  
  @Test
  void getPersonByExistingIdTest() {
    when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));
    
    Person foundPerson = personServiceJpa.getPersonById(1L);
    
    assertNotNull(foundPerson);
    assertEquals(person, foundPerson);
    
    verify(personRepository, times(1)).findById(anyLong());
  }
  
  @Test
  void getPersonByNotExistingIdTest() {
    when(personRepository.findById(anyLong())).thenReturn(Optional.empty());
    Throwable thrown = assertThrows(PersonNotFoundException.class, () -> {
              personServiceJpa.getPersonById(1L);
            });
    assertNotNull(thrown);
    verify(personRepository, times(1)).findById(anyLong());
  }
  
  @Test
  void getAccountsByPersonIdTest() {
    List<Account> accounts = List.of(Account.builder().id(1L).build(), Account.builder().id(2L).build());
    person.setAccounts(accounts);
    
    when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));
    List<Account> expectedAccounts = personServiceJpa.getAccountsByPersonId(1L);
    
    assertNotNull(expectedAccounts);
    assertFalse(expectedAccounts.isEmpty());
    assertEquals(2, expectedAccounts.size());
    assertEquals(accounts, expectedAccounts);
    
    verify(personRepository, times(1)).findById(anyLong());
  }
  
  @Test
  void getAccountsByPersonIdWithoutAccountsTest() {
    person.setAccounts(null);
    
    when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));
    List<Account> expectedAccounts = personServiceJpa.getAccountsByPersonId(1L);
    
    assertNotNull(expectedAccounts);
    assertTrue(expectedAccounts.isEmpty());
    
    verify(personRepository, times(1)).findById(anyLong());
  }
  
  @Test
  void getAccountsByPersonIdWithEmptyAccountsTest() {
    person.setAccounts(Collections.emptyList());
    
    when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));
    List<Account> expectedAccounts = personServiceJpa.getAccountsByPersonId(1L);
    
    assertNotNull(expectedAccounts);
    assertTrue(expectedAccounts.isEmpty());
    
    verify(personRepository, times(1)).findById(anyLong());
  }
  
  @Test
  void getAccountsByPersonIdExceptionTest() {
    person.setAccounts(Collections.emptyList());
  
    when(personRepository.findById(anyLong())).thenReturn(Optional.empty());
    Throwable thrown = assertThrows(PersonNotFoundException.class, () -> {
      personServiceJpa.getPersonById(1L);
    });
    assertNotNull(thrown);
    assertEquals(PersonNotFoundException.class, thrown.getClass());
    verify(personRepository, times(1)).findById(anyLong());
  }
}