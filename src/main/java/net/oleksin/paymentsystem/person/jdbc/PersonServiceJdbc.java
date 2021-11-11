package net.oleksin.paymentsystem.person.jdbc;

import lombok.AllArgsConstructor;
import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountService;
import net.oleksin.paymentsystem.accounttype.AccountType;
import net.oleksin.paymentsystem.exception.PersonNotFoundException;
import net.oleksin.paymentsystem.person.Person;
import net.oleksin.paymentsystem.person.PersonService;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@Profile("jdbcTemplate")
@AllArgsConstructor
public class PersonServiceJdbc implements PersonService {

    private static final String SQL_INSERT =
            "insert into persons(first_name, last_name)" +
                    " values (?, ?)";

    private static final String SQL_SELECT_ALL =
            "select * from persons";

    private static final String SQL_SELECT_PERSON_BY_ID =
            "select * from persons " +
                    " where id = ?";

    private static final String SQL_SELECT_ACCOUNTS_BY_PERSON_ID =
            "select * from accounts" +
                    " inner join account_types" +
                    " on accounts.type_id = account_types.id" +
                    " where accounts.person_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final AccountService accountService;

    @Override
    public Person saveNewPerson(Person person) {
        if(person != null && person.getAccounts() != null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> returnId(connection, person), keyHolder);
            person.setId(keyHolder.getKey().longValue());
            person.getAccounts().forEach(account -> {
                account.setPerson(person);
                accountService.saveNewAccount(account);
            });
            return person;
        }
        return null;
    }

    @Override
    public List<Person> getAllPersons() {
        return jdbcTemplate.query(SQL_SELECT_ALL, new BeanPropertyRowMapper<>(Person.class));
    }

    @Override
    public Person getPersonById(Long id) {
        Person person;
        try {
            person = jdbcTemplate.queryForObject(SQL_SELECT_PERSON_BY_ID, this::mapToPerson, id);
        } catch (EmptyResultDataAccessException e) {
            throw new PersonNotFoundException(String.format("Person with id = %d not found", id));
        }
        return person;
    }
    
    @Override
    public List<Account> getAccountsByPersonId(Long id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SELECT_ACCOUNTS_BY_PERSON_ID, id);

        List<Account> accounts = new ArrayList<>();
        rs.beforeFirst();
        while (rs.next()) {
            AccountType accountType = AccountType.builder()
                    .id(rs.getLong(6))
                    .name(rs.getString(7))
                    .build();
            Account account = Account.builder()
                    .id(rs.getLong(1))
                    .accountNumber(rs.getString(2))
                    .accountType(accountType)
                    .balance(rs.getBigDecimal(3))
                    .build();
            accounts.add(account);
        }

        return accounts;
    }
    
    private Person mapToPerson(ResultSet resultSet, int i) throws SQLException {
        return Person.builder()
                .id(resultSet.getLong(1))
                .firstName(resultSet.getString(2))
                .lastName(resultSet.getString(3))
                .build();
    }

    private PreparedStatement returnId(Connection connection, Person person) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, new String[] { "id" });
        preparedStatement.setString(1, person.getFirstName());
        preparedStatement.setString(2, person.getLastName());
        return preparedStatement;
    }
}
