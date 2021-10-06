package net.oleksin.paymentsystem.account;

import net.oleksin.paymentsystem.person.Person;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@Profile("jdbctamplate")
public class AccountServiceJdbc implements AccountService {

    private static final String SQL_FIND_BY_ID =
            "select * from accounts" +
                    " where id = ?";

    private final JdbcTemplate jdbcTemplate;

    public AccountServiceJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountById(Long id) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, this::mapForAccount, id);
    }


    private Account mapForAccount(ResultSet resultSet, int i) throws SQLException {
        return Account.builder()
                .id(resultSet.getLong(1))
                .accountNumber(resultSet.getString(2))
                .accountType(AccountType.fromString(resultSet.getString(3)))
                .balance(resultSet.getBigDecimal(4))
                .person((Person) resultSet.getObject(5))
                .build();
    }
}
