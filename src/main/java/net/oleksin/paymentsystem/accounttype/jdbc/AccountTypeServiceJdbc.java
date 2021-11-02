package net.oleksin.paymentsystem.accounttype.jdbc;

import lombok.AllArgsConstructor;
import net.oleksin.paymentsystem.accounttype.AccountType;
import net.oleksin.paymentsystem.accounttype.AccountTypeService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;

@Service
@Profile("jdbcTemplate")
@AllArgsConstructor
public class AccountTypeServiceJdbc implements AccountTypeService {
  
  private static final String SQL_INSERT_NEW_ACCOUNT_TYPE =
          "insert into account_types (id, name)" +
                  " values(?, ?)";
  
  private final JdbcTemplate jdbcTemplate;

  @Override
  public AccountType saveNewAccountType(AccountType accountType) {
    return jdbcTemplate.execute(SQL_INSERT_NEW_ACCOUNT_TYPE, getInsertCallBack(accountType));
  }
  
  private PreparedStatementCallback<AccountType> getInsertCallBack(AccountType accountType) {
    return preparedStatement -> {
      preparedStatement.setLong(1, accountType.getId());
      preparedStatement.setString(2, accountType.getName());
      
      preparedStatement.executeUpdate();
      
      return accountType;
    };
  }
}
