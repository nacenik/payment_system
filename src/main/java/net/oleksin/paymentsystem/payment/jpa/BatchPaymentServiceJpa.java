package net.oleksin.paymentsystem.payment.jpa;

import lombok.AllArgsConstructor;
import net.oleksin.paymentsystem.ToResponseConverter;
import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.Account_;
import net.oleksin.paymentsystem.payment.*;
import net.oleksin.paymentsystem.person.Person;
import net.oleksin.paymentsystem.person.Person_;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Metamodel;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile({"springJpaProfile", "default"})
@AllArgsConstructor
public class BatchPaymentServiceJpa implements BatchPaymentService {
  
  private final PaymentService paymentService;
  private final EntityManagerFactory entityManagerFactory;
  private final ToResponseConverter<PaymentJournalDto, PaymentJournal> toResponseConverter;
  
  @Override
  public List<Payment> createNewPayments(List<Payment> payments) {
    List<Payment> newPayments = new ArrayList<>();
    payments.forEach(payment -> {
      Payment newPayment = paymentService.createNewPayment(payment);
      newPayments.add(newPayment);
    });
    return newPayments;
  }

  @Transactional
  @Override
  public List<PaymentJournalDto> getPaymentJournals(Long payerId, Long recipientId, Long srcAccId, Long destAccId) {
    return getPaymentJournal(payerId, recipientId, srcAccId, destAccId)
            .stream()
            .map(toResponseConverter::toResponseDto)
            .collect(Collectors.toList());
  }

  private List<PaymentJournal> getPaymentJournal(Long payerId, Long recipientId, Long srcAccId, Long destAccId) {

    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PaymentJournal> criteriaQuery = criteriaBuilder.createQuery(PaymentJournal.class);
    Metamodel metamodel = entityManager.getMetamodel();
    Root<Payment> rootPayment = criteriaQuery.from(metamodel.entity(Payment.class));

    Join<Payment, Account> srcPaymentJoin = rootPayment.join(Payment_.SOURCE, JoinType.LEFT);
    Join<Payment, Account> dectPaymentJoin = rootPayment.join(Payment_.DESTINATION, JoinType.LEFT);
    Join<Payment, Person> payerPaymentJoin = srcPaymentJoin.join(Account_.PERSON, JoinType.LEFT);
    Join<Payment, Person> recipientPaymentJoin = dectPaymentJoin.join(Account_.PERSON, JoinType.LEFT);
    criteriaQuery.multiselect(rootPayment.get(Payment_.ID), rootPayment.get(Payment_.TIMESTAMP), rootPayment.get(Payment_.AMOUNT),
            srcPaymentJoin.get(Account_.ACCOUNT_NUMBER), dectPaymentJoin.get(Account_.ACCOUNT_NUMBER),
            payerPaymentJoin.get(Person_.FIRST_NAME), payerPaymentJoin.get(Person_.LAST_NAME),
            recipientPaymentJoin.get(Person_.FIRST_NAME), recipientPaymentJoin.get(Person_.LAST_NAME));

    ParameterExpression<Long> srcAccIdExp = criteriaBuilder.parameter(Long.class);
    Predicate srcAccPredicate = criteriaBuilder.equal(srcPaymentJoin.get(Account_.ID), srcAccIdExp);
    Predicate isNullSrcAcc = isNull(srcAccId) ? criteriaBuilder.isNotNull(srcPaymentJoin.get(Account_.ID)) : criteriaBuilder.isNull(srcPaymentJoin.get(Account_.ID));
    Predicate orSrcAccPredicate = criteriaBuilder.or(srcAccPredicate, isNullSrcAcc);

    ParameterExpression<Long> destAccIdExp = criteriaBuilder.parameter(Long.class);
    Predicate destAccPredicate = criteriaBuilder.equal(dectPaymentJoin.get(Account_.ID), destAccIdExp);
    Predicate isNullDestAcc = isNull(destAccId) ? criteriaBuilder.isNotNull(dectPaymentJoin.get(Account_.ID)) : criteriaBuilder.isNull(dectPaymentJoin.get(Account_.ID));
    Predicate orDestAccPredicate = criteriaBuilder.or(destAccPredicate, isNullDestAcc);

    ParameterExpression<Long> payerIdExp = criteriaBuilder.parameter(Long.class);
    Predicate payerPredicate = criteriaBuilder.equal(payerPaymentJoin.get(Person_.ID), payerIdExp);
    Predicate isNullPayer = isNull(payerId) ? criteriaBuilder.isNotNull(payerPaymentJoin.get(Person_.ID)) : criteriaBuilder.isNull(payerPaymentJoin.get(Person_.ID));
    Predicate orPayerPredicate = criteriaBuilder.or(payerPredicate, isNullPayer);

    ParameterExpression<Long> recipientIdExp = criteriaBuilder.parameter(Long.class);
    Predicate recipientPredicate = criteriaBuilder.equal(recipientPaymentJoin.get(Person_.ID), recipientIdExp);
    Predicate isNullRecipient= isNull(recipientId) ? criteriaBuilder.isNotNull(recipientPaymentJoin.get(Person_.ID)) : criteriaBuilder.isNull(recipientPaymentJoin.get(Person_.ID));
    Predicate orRecipientPredicate = criteriaBuilder.or(recipientPredicate, isNullRecipient);

    Predicate and = criteriaBuilder.and(orSrcAccPredicate, orDestAccPredicate, orPayerPredicate, orRecipientPredicate);

    criteriaQuery.where(and);

    TypedQuery<PaymentJournal> query = entityManager.createQuery(criteriaQuery);
    query.setParameter(srcAccIdExp, srcAccId);
    query.setParameter(destAccIdExp, destAccId);
    query.setParameter(payerIdExp, payerId);
    query.setParameter(recipientIdExp, recipientId);
    return query.getResultList();
  }

  private boolean isNull(Object o) {
    return o == null;
  }
}
