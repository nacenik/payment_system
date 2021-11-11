package net.oleksin.paymentsystem.accounttype.jpa;

import net.oleksin.paymentsystem.accounttype.AccountType;
import net.oleksin.paymentsystem.accounttype.AccountType_;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountTypeServiceJpaTest {
  
  @Mock
  private AccountTypeRepository accountTypeRepository;

  @Mock
  private EntityManager entityManager;
  
  @InjectMocks
  private AccountTypeServiceJpa accountTypeServiceJpa;

  @Mock
  private CriteriaBuilder criteriaBuilder;

  @Mock
  private CriteriaQuery cq;

  @Mock
  private Root from;

  @Mock
  private TypedQuery tq;

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
    when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(any())).thenReturn(cq);
    when(cq.from(AccountType.class)).thenReturn(from);
    when(entityManager.createQuery(cq)).thenReturn(tq);
    when(tq.getSingleResult()).thenReturn(0L);

    AccountType expected = accountTypeServiceJpa.saveNewAccountType(AccountType.builder().name("simple").build());
    verify(cq).select(criteriaBuilder.count(from));
    verify(cq).where(criteriaBuilder.equal(any(), anyString()));
    verify(tq).getSingleResult();

    assertNotNull(expected);
    assertEquals(accountType, expected);

    verify(accountTypeRepository).save(any());
  }
  
  @Test
  void saveNewAccountTypeWhenExistTest() {
    when(accountTypeRepository.findAll()).thenReturn(List.of(accountType));
    when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(any())).thenReturn(cq);
    when(cq.from(AccountType.class)).thenReturn(from);
    when(entityManager.createQuery(cq)).thenReturn(tq);
    when(tq.getSingleResult()).thenReturn(1L);

    AccountType expected = accountTypeServiceJpa.saveNewAccountType(AccountType.builder().name("simple").build());
    verify(cq).select(criteriaBuilder.count(from));
    verify(cq).where(criteriaBuilder.equal(any(), anyString()));
    verify(tq).getSingleResult();

    assertNotNull(expected);
    assertEquals(accountType, expected);

    verify(accountTypeRepository).findAll();
  }
  
}