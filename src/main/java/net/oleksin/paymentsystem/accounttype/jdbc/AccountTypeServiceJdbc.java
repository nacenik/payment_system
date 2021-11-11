package net.oleksin.paymentsystem.accounttype.jdbc;

import lombok.AllArgsConstructor;
import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.accounttype.AccountType;
import net.oleksin.paymentsystem.accounttype.AccountTypeService;
import net.oleksin.paymentsystem.person.Person;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@Profile("jdbcTemplate")
@AllArgsConstructor
public class AccountTypeServiceJdbc implements AccountTypeService {
  
  private static final String SQL_INSERT_NEW_ACCOUNT_TYPE =
          "insert into account_types (name)" +
                  " values(?)";

  private static final String SQL_COUNT =
          "select count(id) from account_types" +
                  " where account_types.name = ?";

  private static final String SQL_SELECT =
          "select * from account_types" +
                  " where account_types.name = ?";
  
  private final JdbcTemplate jdbcTemplate;

  @Override
  public AccountType saveNewAccountType(AccountType accountType) {
    Integer integer = jdbcTemplate.queryForObject(SQL_COUNT, Integer.TYPE, accountType.getName());
    if (integer != null || integer != 0) {
      return jdbcTemplate.queryForObject(SQL_SELECT, this::mapToAccountType, accountType.getName());
    }
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> returnId(connection, accountType), keyHolder);
    accountType.setId(keyHolder.getKey().longValue());
    return accountType;
  }

  private AccountType mapToAccountType(ResultSet resultSet, int i) throws SQLException {
    return AccountType.builder()
            .id(resultSet.getLong("id"))
            .name(resultSet.getString("name"))
            .build();
  }

  private PreparedStatement returnId(Connection connection, AccountType accountType) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_NEW_ACCOUNT_TYPE, new String[] { "id" });
    preparedStatement.setString(1, accountType.getName());
    
    return preparedStatement;
  }
}
