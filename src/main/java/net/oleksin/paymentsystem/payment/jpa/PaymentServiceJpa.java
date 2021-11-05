package net.oleksin.paymentsystem.payment.jpa;

import lombok.AllArgsConstructor;
import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.jpa.AccountRepository;
import net.oleksin.paymentsystem.exception.PaymentNotFoundException;
import net.oleksin.paymentsystem.payment.AbstractPaymentService;
import net.oleksin.paymentsystem.payment.Payment;
import net.oleksin.paymentsystem.payment.PaymentService;
import net.oleksin.paymentsystem.payment.Status;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Profile({ "default", "springJpaProfile"})
public class PaymentServiceJpa extends AbstractPaymentService implements PaymentService {

  private final PaymentRepository paymentRepository;
  private final AccountRepository accountRepository;

  @Transactional
  @Override
  public Payment createNewPayment(Payment payment) {
    if (super.isPaymentNull(payment)) {
      throw new PaymentNotFoundException("Payment doesn't contains valid fields");
    }
    Optional<Account> sourceAccountOptional = accountRepository.findById(payment.getSource().getId());
    Optional<Account> destinationAccountOptional = accountRepository.findById(payment.getDestination().getId());

    Payment newPayment = super.createNewPayment(payment, sourceAccountOptional, destinationAccountOptional);

    if (newPayment.getStatus().equals(Status.ok)) {
      accountRepository.save(sourceAccountOptional.get());
      accountRepository.save(destinationAccountOptional.get());
    }

    return paymentRepository.save(newPayment);

  }

}
