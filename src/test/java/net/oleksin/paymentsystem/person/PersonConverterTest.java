package net.oleksin.paymentsystem.person;

import net.oleksin.paymentsystem.Converter;
import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonConverterTest {
  
  @Mock
  private Converter<AccountDto, AccountDto, Account> accountConverter;
  
  @InjectMocks
  private PersonConverter personConverter;
  
  private Person person;
  private PersonRequestDto personRequestDto;
  private Account account;
  private AccountDto accountDto;
  
  @BeforeEach
  void setUp() {
    accountDto = AccountDto.builder().id(1L).build();
    account = Account.builder().id(1L).build();
    person = Person.builder()
            .id(1L)
            .firstName("test")
            .lastName("test")
            .accounts(Set.of(account))
            .build();
  
    personRequestDto = PersonRequestDto.builder()
            .id(person.getId())
            .firstName(person.getFirstName())
            .lastName(person.getLastName())
            .accounts(Set.of(accountDto))
            .build();
  }
  
  @Test
  void toResponseDtoTest() {
    PersonResponseDto personResponseDto = personConverter.toResponseDto(person);
    
    assertNotNull(personResponseDto);
    assertEquals(personResponseDto.getId(), person.getId());
    assertEquals(personResponseDto.getFirstName(), person.getFirstName());
    assertEquals(personResponseDto.getLastName(), person.getLastName());
  }
  
  @Test
  void fromRequestDtoTest() {
    when(accountConverter.fromRequestDto(any()))
            .thenReturn(account);
    Person expected = personConverter.fromRequestDto(personRequestDto);
    
    assertNotNull(expected);
    assertEquals(personRequestDto.getId(), person.getId());
    assertEquals(personRequestDto.getFirstName(), person.getFirstName());
    assertEquals(personRequestDto.getLastName(), person.getLastName());
    assertEquals(personRequestDto.getAccounts().size(), person.getAccounts().size());
    
  }
}