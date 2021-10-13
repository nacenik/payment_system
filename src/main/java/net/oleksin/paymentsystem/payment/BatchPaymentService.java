package net.oleksin.paymentsystem.payment;

import java.util.List;

public interface BatchPaymentService {
  List<Payment> createNewPayments(List<Payment> payments);
  List<PaymentJournalDto> getPaymentJournals(Long payerId, Long recipientId, Long srcAccId, Long destAccId);
}
