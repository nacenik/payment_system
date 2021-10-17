package net.oleksin.paymentsystem.account.jpa;

import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.accounttype.AccountType;
import net.oleksin.paymentsystem.exception.AccountNotFoundException;
import net.oleksin.paymentsystem.person.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceJpaTest {
  
  @Mock
  private AccountRepository accountRepository;
  
  @InjectMocks
  private AccountServiceJpa accountServiceJpa;
  
  private Account account;
  
  @BeforeEach
  void setUp() {
    account = Account.builder()
            .id(1L)
            .accountType(AccountType.builder().id(1L).build())
            .person(Person.builder().id(1L).build())
            .balance(new BigDecimal(1234))
            .accountNumber("test")
            .build();
  }
  
  @Test
  void getAccountByIdTest() {
    when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
    
    Account expected = accountServiceJpa.getAccountById(1L);
    
    assertNotNull(expected);
    assertEquals(account, expected);
    
    verify(accountRepository, times(1)).findById(anyLong());
  }
  
  @Test
  void getAccountByIdExceptionTest() {
    when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());
    
    Throwable throwable = assertThrows(AccountNotFoundException.class, () -> {
      accountServiceJpa.getAccountById(1L);
    });
    
    assertNotNull(throwable);
    assertEquals(AccountNotFoundException.class, throwable.getClass());
    verify(accountRepository, times(1)).findById(anyLong());
  }
}