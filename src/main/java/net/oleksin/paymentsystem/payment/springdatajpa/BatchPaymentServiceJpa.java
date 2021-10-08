package net.oleksin.paymentsystem.payment.springdatajpa;

import net.oleksin.paymentsystem.payment.BatchPaymentService;
import net.oleksin.paymentsystem.payment.Payment;
import net.oleksin.paymentsystem.payment.PaymentService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Profile({"springJpaProfile", "default"})
public class BatchPaymentServiceJpa implements BatchPaymentService {
  
  private final PaymentService paymentService;
  
  public BatchPaymentServiceJpa(PaymentService paymentService) {
    this.paymentService = paymentService;
  }
  
  @Override
  public Set<Payment> createNewPayments(Set<Payment> payments) {
    Set<Payment> newPayments = new HashSet<>();
    payments.forEach(payment -> {
      Payment newPayment = paymentService.createNewPayment(payment);
      newPayments.add(newPayment);
    });
    return newPayments;
  }
}
