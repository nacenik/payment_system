package net.oleksin.paymentsystem.payment.jpa;

import net.oleksin.paymentsystem.payment.BatchPaymentService;
import net.oleksin.paymentsystem.payment.Payment;
import net.oleksin.paymentsystem.payment.PaymentJournalDto;
import net.oleksin.paymentsystem.payment.PaymentService;
import net.oleksin.paymentsystem.person.PersonResponseDto;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile({"springJpaProfile", "default"})
public class BatchPaymentServiceJpa implements BatchPaymentService {
  
  
  private final PaymentService paymentService;
  private final PaymentRepository paymentRepository;
  
  public BatchPaymentServiceJpa(PaymentService paymentService, PaymentRepository paymentRepository) {
    this.paymentService = paymentService;
    this.paymentRepository = paymentRepository;
  }
  
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
    List<Object[]> objects = paymentRepository.getPaymentJournal(payerId, recipientId, srcAccId, destAccId);
    return objects.stream()
            .map(this::getJournal)
            .collect(Collectors.toList());
  }
  
  private PaymentJournalDto getJournal(Object[] objects) {
    if (objects.length == 9) {
      return PaymentJournalDto.builder()
              .paymentId(Long.parseLong(objects[0].toString()))
              .timestamp(Timestamp.valueOf((objects[1].toString())).toLocalDateTime())
              .sourceAccountNumber(objects[2].toString())
              .destinationAccountNumber(objects[3].toString())
              .amount(new BigDecimal(objects[4].toString()))
              .payer(PersonResponseDto.builder().firstName(objects[5].toString()).lastName(objects[6].toString()).build())
              .recipient(PersonResponseDto.builder().firstName(objects[7].toString()).lastName(objects[8].toString()).build())
              .build();
    }
    return null;
  }
}
