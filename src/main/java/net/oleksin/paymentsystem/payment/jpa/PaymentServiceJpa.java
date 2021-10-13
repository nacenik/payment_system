package net.oleksin.paymentsystem.payment.jpa;

import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.jpa.AccountRepository;
import net.oleksin.paymentsystem.exception.AccountNotFoundException;
import net.oleksin.paymentsystem.exception.PaymentNotFoundException;
import net.oleksin.paymentsystem.payment.Payment;
import net.oleksin.paymentsystem.payment.PaymentService;
import net.oleksin.paymentsystem.payment.Status;
import net.oleksin.paymentsystem.person.jpa.PersonRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Profile({"springJpaProfile", "default"})
public class PaymentServiceJpa implements PaymentService {
  
  private final PaymentRepository paymentRepository;
  private final AccountRepository accountRepository;
  private final PersonRepository personRepository;
  
  public PaymentServiceJpa(PaymentRepository paymentRepository, AccountRepository accountRepository, PersonRepository personRepository) {
    this.paymentRepository = paymentRepository;
    this.accountRepository = accountRepository;
    this.personRepository = personRepository;
  }
  
  @Transactional
  @Override
  public Payment createNewPayment(Payment payment) {
    if (isPaymentNull(payment)) {
      throw new PaymentNotFoundException();
    }
    Account sourceAccount = accountRepository.getById(payment.getSource().getId());
    Account destinationAccount = accountRepository.getById(payment.getDestination().getId());
    
    BigDecimal sourceAmount = sourceAccount.getBalance();
    BigDecimal destinationAmount = destinationAccount.getBalance();
  
    if (sourceAmount == null || destinationAmount == null) {
      throw new AccountNotFoundException();
    } else {
      Payment newPayment = new Payment();
      newPayment.setSource(sourceAccount);
      newPayment.setDestination(destinationAccount);
      newPayment.setReason(payment.getReason());
      newPayment.setAmount(payment.getAmount());
      
      if(sourceAccount.getBalance().compareTo(newPayment.getAmount()) > 0) {
        newPayment.setStatus(Status.ERROR);
      } else {
        sourceAccount.setBalance(sourceAmount.subtract(newPayment.getAmount()));
        destinationAccount.setBalance(destinationAmount.add(newPayment.getAmount()));
        newPayment.setStatus(Status.OK);
      }
  
      newPayment.setTimestamp(LocalDateTime.now());
      
      return paymentRepository.save(newPayment);
    }
    
  }

  private boolean isPaymentNull(Payment payment) {
    return payment.getSource() == null
            || payment.getSource().getId() == null
            || payment.getDestination() == null
            || payment.getDestination().getId() == null
            || payment.getAmount() == null
            || payment.getReason() == null;
  }
  
}
