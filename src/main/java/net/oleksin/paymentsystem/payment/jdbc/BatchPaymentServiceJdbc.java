package net.oleksin.paymentsystem.payment.jdbc;

import net.oleksin.paymentsystem.payment.BatchPaymentService;
import net.oleksin.paymentsystem.payment.Payment;
import net.oleksin.paymentsystem.payment.PaymentJournalDto;
import net.oleksin.paymentsystem.payment.PaymentService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Profile("jdbcTemplate")
public class BatchPaymentServiceJdbc implements BatchPaymentService {
    private final String SQL = "SELECT payments.id, payments.amount, payments.source_id, payments.destination_id, payments.timestamp," +
            " source_account.account_number as source_account_number," +
            " destination_account.account_number as destination_account_number," +
            " payer.first_name as payer_first_name," +
            " payer.last_name as payer_last_name," +
            " recipient.first_name as recipient_first_name, " +
            " recipient.last_name as recipient_last_name" +
            " FROM payments" +
            " join accounts as source_account" +
            " on payments.source_id = source_account.id" +
            " join accounts as destination_account" +
            " on payments.destination_id = destination_account.id" +
            " join persons as payer" +
            " on source_account.person_id = payer.id" +
            " join persons as recipient" +
            " on destination_account.person_id = recipient.id" +
            " where payments.destination_id = destAccId" +
            " and payments.source_id = scrAccId" +
            " and source_account.person_id = payerId" +
            " and destination_account.person_id = recipientId";
    
    private  final PaymentService paymentService;

    public BatchPaymentServiceJdbc(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public List<Payment> createNewPayments(List<Payment> payments) {
        List<Payment> newPayments = new ArrayList<>();
        payments.forEach(payment -> {
            newPayments.add(paymentService.createNewPayment(payment));
        });
        return newPayments;
    }
    
    @Override
    public List<PaymentJournalDto> getPaymentJournals(Long payerId, Long recipientId, Long srcAccId, Long destAccId) {
        return null;
    }
}
