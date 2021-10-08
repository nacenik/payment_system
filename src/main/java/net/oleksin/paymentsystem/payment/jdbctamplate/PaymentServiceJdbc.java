package net.oleksin.paymentsystem.payment.jdbctamplate;

import net.oleksin.paymentsystem.payment.PaymentService;
import net.oleksin.paymentsystem.payment.Payment;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Profile("jdbctamplate")
public class PaymentServiceJdbc implements PaymentService {

    private static final String SQL_INSERT =
            "insert into payments(id, source_id, destination_id, amount, reason, timestamp, status)" +
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
            preparedStatement.setObject(2, payment.getSource());
            preparedStatement.setObject(3, payment.getDestination());
            preparedStatement.setBigDecimal(4, payment.getAmount());
            preparedStatement.setString(5, payment.getReason());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(payment.getTimestamp()));
            preparedStatement.setString(7, payment.getStatus().getStatusString());

            preparedStatement.executeUpdate();

            return payment;
        };
    }
}
