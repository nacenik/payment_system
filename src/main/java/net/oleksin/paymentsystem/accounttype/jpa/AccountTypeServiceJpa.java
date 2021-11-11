package net.oleksin.paymentsystem.accounttype.jpa;

import lombok.AllArgsConstructor;
import net.oleksin.paymentsystem.accounttype.AccountType;
import net.oleksin.paymentsystem.accounttype.AccountTypeService;
import net.oleksin.paymentsystem.accounttype.AccountType_;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Service
@Profile({ "default", "springJpaProfile"})
@AllArgsConstructor
public class AccountTypeServiceJpa implements AccountTypeService {
  private final AccountTypeRepository accountTypeRepository;
  private final EntityManager entityManager;

  @Transactional
  @Override
  public AccountType saveNewAccountType(AccountType accountType) {
    if(!exists(AccountType.class, accountType.getName())) {
      return accountTypeRepository.save(accountType);
    }
    return findByName(accountType);
  }

  private AccountType findByName(AccountType accountType) {
    return  accountTypeRepository.findAll()
            .stream()
            .filter(at -> at.getName().equals(accountType.getName()))
            .findFirst()
            .orElse(accountType);
  }

  private  <E extends AccountType> boolean exists(final Class<E> entityClass, final String name) {
    final CriteriaBuilder cb = entityManager.getCriteriaBuilder();

    final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    final Root<E> from = cq.from(entityClass);

    cq.select(cb.count(from));
    cq.where(cb.equal(from.get(AccountType_.name), name));

    TypedQuery<Long> tq = entityManager.createQuery(cq);
    return tq.getSingleResult() > 0;
  }
}
