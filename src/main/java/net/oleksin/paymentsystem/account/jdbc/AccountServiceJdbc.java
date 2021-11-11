package net.oleksin.paymentsystem.account.jdbc;

import lombok.AllArgsConstructor;
import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountService;
import net.oleksin.paymentsystem.accounttype.AccountTypeService;
import net.oleksin.paymentsystem.payment.Payment;
import net.oleksin.paymentsystem.person.Person;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
@Profile("jdbcTemplate")
@AllArgsConstructor
public class AccountServiceJdbc implements AccountService {

    private static final String SQL_FIND_BY_ID =
            "select * from accounts" +
                    " where id = ?";

    private static final  String SQL_INSERT =
            "insert into accounts(account_number, type_id, balance, person_id)" +
                    " values (?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;
    private final AccountTypeService accountTypeService;

    @Override
    public Account getAccountById(Long id) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, this::mapForAccount, id);
    }

    @Override
    public Account saveNewAccount(Account account) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        account.setAccountType(accountTypeService.saveNewAccountType(account.getAccountType()));
        jdbcTemplate.update(connection -> returnId(connection, account), keyHolder);
        account.setId(keyHolder.getKey().longValue());
        return account;
    }


    private Account mapForAccount(ResultSet resultSet, int i) throws SQLException {
        return Account.builder()
                .id(resultSet.getLong(1))
                .accountNumber(resultSet.getString(2))
                .balance(resultSet.getBigDecimal(4))
                .person((Person) resultSet.getObject(5))
                .build();
    }

    private PreparedStatement returnId(Connection connection, Account account) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, new String[] { "id" });
        preparedStatement.setString(1, account.getAccountNumber());
        preparedStatement.setLong(2, account.getAccountType().getId());
        preparedStatement.setBigDecimal(3, account.getBalance());
        preparedStatement.setLong(4, account.getPerson().getId());
        
        return preparedStatement;
    }
}
