package net.oleksin.paymentsystem.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface PaymentService {
  Long createNewPayment(Payment payment);
  
  Set<Payment> createNewPayments(Set<Payment> payments);
  
  Set<Payment> getPayments(Set<Payment> payments);
}
