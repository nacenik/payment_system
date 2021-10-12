package net.oleksin.paymentsystem.payment.jdbc;

import net.oleksin.paymentsystem.payment.PaymentService;
import net.oleksin.paymentsystem.payment.Payment;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Profile("jdbcTemplate")
public class PaymentServiceJdbc implements PaymentService {

    private static final String SQL_INSERT =
            "insert into payments(id, amount, reason, timestamp, status, source_id, destination_id, )" +
                    " values(?, ?, ?, ?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    public PaymentServiceJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Payment createNewPayment(Payment payment) {
        return jdbcTemplate.execute(SQL_INSERT, getInsertCallBack(payment));
    }

    private PreparedStatementCallback<Payment> getInsertCallBack(Payment payment) {
        return preparedStatement -> {
            preparedStatement.setLong(1, payment.getId());
            preparedStatement.setBigDecimal(2, payment.getAmount());
            preparedStatement.setString(3, payment.getReason());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(payment.getTimestamp()));
            preparedStatement.setString(5, payment.getStatus().getStatusString());
            preparedStatement.setObject(6, payment.getSource());
            preparedStatement.setObject(7, payment.getDestination());

            preparedStatement.executeUpdate();

            return payment;
        };
    }
}
