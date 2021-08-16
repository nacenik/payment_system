package net.oleksin.paymentsystem.model;

import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountService;
import net.oleksin.paymentsystem.person.Person;
import net.oleksin.paymentsystem.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile({"springJpaProfile", "default"})
public class PaymentServiceJpa implements PaymentService {
  
  private final PaymentRepository paymentRepository;
  private final AccountService accountService;
  private final PersonService personService;
  
  @Autowired
  public PaymentServiceJpa(PaymentRepository paymentRepository, AccountService accountService, PersonService personService) {
    this.paymentRepository = paymentRepository;
    this.accountService = accountService;
    this.personService = personService;
  }
  
  @Override
  public Long createNewPayment(Payment payment) {
    return saveNewPayment(payment).getId();
  }
  
  
  @Override
  public Set<Payment> createNewPayments(Set<Payment> payments) {
    return payments.stream()
            .map(payment -> {
              Payment pay = saveNewPayment(payment);
              return new Payment.Builder()
                      .id(pay.getId())
                      .status(pay.getStatus())
                      .build();
            })
            .collect(Collectors.toSet());
  }
  
  @Override
  public Set<Payment> getPayments(Set<Payment> payments) {
    return payments.stream()
            .map(this::getPayment)
            .collect(Collectors.toSet());
  }
  
  private Payment getPayment(Payment payment) {
    Account sourceAccount = accountService.getAccountById(payment.getSource().getId());
    Account destAccount = accountService.getAccountById(payment.getDestination().getId());
    Person payer = personService.getPersonById(sourceAccount.getPerson().getId());
    Person recipient = personService.getPersonById(destAccount.getPerson().getId());
    
    return paymentRepository.findAll().stream().filter(pay -> pay.getSource().getId().equals(sourceAccount.getId())
            && pay.getDestination().getId().equals(destAccount.getId())
            && pay.getSource().getPerson().getId().equals(payer.getId())
            && pay.getDestination().getPerson().getId().equals(recipient.getId()))
            .findFirst()
            .orElseThrow();
  }
  
  private Payment saveNewPayment(Payment payment) {
    Account sourceAccount = accountService.getAccountById(payment.getSource().getId());
    Account destinationAccount = accountService.getAccountById(payment.getDestination().getId());
    
    String status;
    if (sourceAccount.getBalance().compareTo(payment.getAmount()) >= 0) {
      sourceAccount.setBalance(sourceAccount.getBalance().subtract(payment.getAmount()));
      destinationAccount.setBalance(destinationAccount.getBalance().add(payment.getAmount()));
      status = "ok";
    } else {
      status = "error";
    }
    
    Payment newPayment = new Payment.Builder()
            .sourceAccount(sourceAccount)
            .destinationAccount(destinationAccount)
            .amount(payment.getAmount())
            .reason(payment.getReason())
            .status(status)
            .build();
    payment.setTimestamp(LocalDateTime.now());
    
    return paymentRepository.save(newPayment);
  }
}
