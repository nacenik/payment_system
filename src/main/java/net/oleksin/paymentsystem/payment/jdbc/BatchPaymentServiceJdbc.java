package net.oleksin.paymentsystem.payment.jdbc;

import net.oleksin.paymentsystem.payment.BatchPaymentService;
import net.oleksin.paymentsystem.payment.Payment;
import net.oleksin.paymentsystem.payment.PaymentJournalDto;
import net.oleksin.paymentsystem.payment.PaymentService;
import net.oleksin.paymentsystem.person.PersonResponseDto;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Profile("jdbcTemplate")
public class BatchPaymentServiceJdbc implements BatchPaymentService {
    private static final String NOT = "not";
    private static final String EMPTY = "";
    private static final String SQL = "SELECT payments.id, payments.amount, payments.source_id, payments.destination_id, payments.timestamp," +
            " source_account.account_number as source_account_number," +
            " destination_account.account_number as destination_account_number," +
            " payer.first_name as payer_first_name," +
            " payer.last_name as payer_last_name," +
            " recipient.first_name as recipient_first_name, " +
            " recipient.last_name as recipient_last_name" +
            " FROM payments" +
            " left join accounts as source_account" +
            " on payments.source_id = source_account.id" +
            " left join accounts as destination_account" +
            " on payments.destination_id = destination_account.id" +
            " left join persons as payer" +
            " on source_account.person_id = payer.id" +
            " left join persons as recipient" +
            " on destination_account.person_id = recipient.id" +
            " where payments.source_id = :srcAccId or payments.source_id is %s null" +
            " and payments.destination_id = :destAccId or payments.destination_id is %s null" +
            " and source_account.person_id = :payerId or source_account.person_id  is %s null" +
            " and destination_account.person_id = :recipientId or destination_account.person_id is %s null";
    
    private final PaymentService paymentService;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BatchPaymentServiceJdbc(PaymentService paymentService, NamedParameterJdbcTemplate jdbcTemplate) {
        this.paymentService = paymentService;
        this.jdbcTemplate = jdbcTemplate;
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
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("srcAccId", srcAccId);
        parameters.addValue("destAccId", destAccId);
        parameters.addValue("payerId", payerId);
        parameters.addValue("recipientId", recipientId);
        String newSql = String.format(SQL,
                srcAccId != null ? EMPTY : NOT,
                destAccId != null ? EMPTY : NOT,
                payerId != null ? EMPTY : NOT,
                recipientId != null ? EMPTY : NOT);
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(newSql, parameters.getValues());
        return mapToPaymentJournal(sqlRowSet);
    }

    private List<PaymentJournalDto> mapToPaymentJournal(SqlRowSet sqlRowSet) {
        List<PaymentJournalDto> list = new ArrayList<>();
        sqlRowSet.beforeFirst();
        while (sqlRowSet.next()) {
            list.add(PaymentJournalDto.builder()
                    .paymentId(sqlRowSet.getLong("id"))
                    .timestamp(sqlRowSet.getTimestamp("timestamp").toLocalDateTime())
                    .sourceAccountNumber(sqlRowSet.getString("source_account_number"))
                    .destinationAccountNumber(sqlRowSet.getString("destination_account_number"))
                    .amount(sqlRowSet.getBigDecimal("amount"))
                    .payer(PersonResponseDto.builder().firstName(sqlRowSet.getString("payer_first_name")).lastName(sqlRowSet.getString("payer_last_name")).build())
                    .recipient(PersonResponseDto.builder().firstName(sqlRowSet.getString("recipient_first_name")).lastName(sqlRowSet.getString("recipient_last_name")).build())
                    .build());
        }
        return list;
    }

}
