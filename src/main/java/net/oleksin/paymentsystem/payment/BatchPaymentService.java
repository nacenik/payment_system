package net.oleksin.paymentsystem.payment;

import java.util.Set;

public interface BatchPaymentService {
  Set<Payment> createNewPayments(Set<Payment> payments);
}
