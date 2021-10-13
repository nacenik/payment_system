package net.oleksin.paymentsystem.payment.jpa;

import net.oleksin.paymentsystem.payment.Payment;
import net.oleksin.paymentsystem.payment.PaymentJournalDto;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
  @Modifying
  @Query(value = "select payments.id, payments.timestamp," +
          " source_account.account_number as source_account_number," +
          " destination_account.account_number as destination_account_number," +
          " payments.amount," +
          " payer.first_name as payer_first_name," +
          " payer.last_name as payer_last_name," +
          " recipient.first_name as recipient_first_name, " +
          " recipient.last_name as recipient_last_name" +
          " from payments" +
          " join accounts as source_account" +
          " on payments.source_id = source_account.id" +
          " join accounts as destination_account" +
          " on payments.destination_id = destination_account.id" +
          " join persons as payer" +
          " on source_account.person_id = payer.id" +
          " join persons as recipient" +
          " on destination_account.person_id = recipient.id" +
          " where source_account.person_id = ?" +
          " and destination_account.person_id = ?" +
          " and payments.source_id = ?" +
          " and payments.destination_id = ?",
          nativeQuery = true)
  List<Object[]> getPaymentJournal(Long payerId, Long recipientId, Long srcAccId, Long destAccId);
}
