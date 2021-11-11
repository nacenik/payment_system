package net.oleksin.paymentsystem.payment.jdbc;

import lombok.Data;
import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.accounttype.AccountType;
import net.oleksin.paymentsystem.exception.PaymentNotFoundException;
import net.oleksin.paymentsystem.payment.AbstractPaymentService;
import net.oleksin.paymentsystem.payment.Payment;
import net.oleksin.paymentsystem.payment.PaymentService;
import net.oleksin.paymentsystem.payment.Status;
import net.oleksin.paymentsystem.person.Person;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

@Service
@Profile("jdbcTemplate")
@Data
public class PaymentServiceJdbc extends AbstractPaymentService implements PaymentService {

    private static final String SQL_INSERT =
            "insert into payments(amount, reason, timestamp, status, source_id, destination_id)" +
                    " values(?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_ACCOUNT =
            "select * from accounts" +
                    " inner join account_types" +
                    " on accounts.type_id = account_types.id" +
                    " inner join persons" +
                    " on accounts.person_id = persons.id" +
                    " where accounts.id = ?";

    public static final String SQL_UPDATE_ACCOUNT =
            "update accounts set balance = ? " +
                    "where id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Payment createNewPayment(Payment payment) {
        if (super.isPaymentNull(payment)) {
            throw new PaymentNotFoundException("Payment doesn't contains valid fields");
        }

        Optional<Account> sourceAccount = Optional.ofNullable(jdbcTemplate.query(SQL_FIND_ACCOUNT,
                getResultSetExtractor(),
                payment.getSource().getId()));

        Optional<Account> destinationAccount = Optional.ofNullable(jdbcTemplate.query(SQL_FIND_ACCOUNT,
                getResultSetExtractor(),
                payment.getDestination().getId()));

        Payment newPayment = super.createNewPayment(payment, sourceAccount, destinationAccount);

        if (newPayment.getStatus().equals(Status.ok)) {
            updateAccountData(sourceAccount.get());
            updateAccountData(destinationAccount.get());
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> returnId(connection, newPayment), keyHolder);
        newPayment.setId(keyHolder.getKey().longValue());
        return newPayment;
    }

    private ResultSetExtractor<Account> getResultSetExtractor() {
        return resultSet -> {
            if(resultSet.next()) {
                return Account.builder()
                        .id(resultSet.getLong(1))
                        .accountNumber(resultSet.getString("account_number"))
                        .balance(resultSet.getBigDecimal("balance"))
                        .accountType(AccountType.builder()
                                .id(resultSet.getLong("type_id"))
                                .name(resultSet.getString("name"))
                                .build())
                        .person(Person.builder()
                                .id(resultSet.getLong("person_id"))
                                .firstName(resultSet.getString("first_name"))
                                .lastName(resultSet.getString("last_name")).build())
                        .build();
            }
            return null;
        };
    }

    private PreparedStatement returnId(Connection connection, Payment newPayment) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, new String[] { "id" });
        preparedStatement.setBigDecimal(1, newPayment.getAmount());
        preparedStatement.setString(2, newPayment.getReason());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(newPayment.getTimestamp()));
        preparedStatement.setString(4, newPayment.getStatus().getName());
        preparedStatement.setLong(5, newPayment.getSource().getId());
        preparedStatement.setLong(6, newPayment.getDestination().getId());
        return preparedStatement;
    }

    private void updateAccountData(Account account) {
        jdbcTemplate.update(SQL_UPDATE_ACCOUNT,
                account.getBalance(),
                account.getId());

    }


}
