package net.oleksin.paymentsystem.payment;

import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountRepository;
import net.oleksin.paymentsystem.exception.AccountNotFoundException;
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
  
  public PaymentServiceJpa(PaymentRepository paymentRepository, AccountRepository accountRepository) {
    this.paymentRepository = paymentRepository;
    this.accountRepository = accountRepository;
  }
  
  @Transactional
  @Override
  public Payment createNewPayment(Payment payment) {
    if (payment == null) {
      throw new AccountNotFoundException();
    }
    Account sourceAccount = accountRepository.getById(payment.getSource().getId());
    Account destinationAccount = accountRepository.getById(payment.getSource().getId());
    
    BigDecimal sourceAmount = sourceAccount.getBalance();
    BigDecimal destinationAmount = destinationAccount.getBalance();
  
    if (sourceAmount == null || destinationAmount == null) {
      throw new AccountNotFoundException();
    } else {
      Payment newPayment = new Payment();
      newPayment.setSource(sourceAccount);
      newPayment.setDestination(destinationAccount);
      newPayment.setReason(newPayment.getReason());
      
      if(sourceAccount.getBalance().compareTo(newPayment.getAmount()) >= 0) {
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
  
}
