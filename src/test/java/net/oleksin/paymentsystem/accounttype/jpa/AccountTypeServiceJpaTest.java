package net.oleksin.paymentsystem.accounttype.jpa;

import net.oleksin.paymentsystem.accounttype.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountTypeServiceJpaTest {
  
  @Mock
  private AccountTypeRepository accountTypeRepository;
  
  @InjectMocks
  private AccountTypeServiceJpa accountTypeServiceJpa;
  
  private AccountType accountType;
  
  @BeforeEach
  void setUp() {
    accountType = AccountType.builder()
            .id(1L)
            .name("simple")
            .build();
  }
  
  @Test
  void saveNewAccountTypeTest() {
    when(accountTypeRepository.save(any())).thenReturn(accountType);
    
    AccountType expected = accountTypeServiceJpa.saveNewAccountType(AccountType.builder().id(1L).build());
    
    assertNotNull(expected);
    assertEquals(accountType, expected);
    
    verify(accountTypeRepository).save(any());
  }
  
}