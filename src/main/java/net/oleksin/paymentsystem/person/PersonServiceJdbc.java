package net.oleksin.paymentsystem.person;

import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountType;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Profile("jdbctamplate")
public class PersonServiceJdbc implements PersonService {

    private static final String SQL_INSERT =
            "insert into persons(id, first_name, last_name)" +
                    " values (?, ?, ?)";

    private static final String SQL_SELECT_ALL =
            "select * from persons";

    private static final String SQL_SELECT_PERSON_BY_ID =
            "select * from persons " +
                    " where id = ?";

    private static final String SQL_SELECT_ACCOUNTS_BY_PERSON_ID =
            "select * from accounts" +
                    " where person_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public PersonServiceJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Person saveNewPerson(Person person) {
        return jdbcTemplate.execute(SQL_INSERT, getInsertCallBack(person));
    }

    @Override
    public List<Person> getAllPersons() {
        return jdbcTemplate.query(SQL_SELECT_ALL, new BeanPropertyRowMapper<>(Person.class));
    }

    @Override
    public Person getPersonById(Long id) {
        return jdbcTemplate.queryForObject(SQL_SELECT_PERSON_BY_ID, this::matToPerson, id);
    }

    @Override
    public Set<Account> getAccountByPersonId(Long id) {
        return jdbcTemplate.queryForObject(SQL_SELECT_ACCOUNTS_BY_PERSON_ID, (rs, rovNum) -> {
            Set<Account> accounts = new HashSet<>();
            while (rs.next()) {
                Account account = Account.builder()
                        .id(rs.getLong(1))
                        .accountNumber(rs.getString(2))
                        .accountType(AccountType.fromString(rs.getString(3)))
                        .balance(rs.getBigDecimal(4))
                        .build();
                accounts.add(account);
            }
            return accounts;
        }, id);
    }

    private Person matToPerson(ResultSet resultSet, int i) throws SQLException {
        return Person.builder()
                .id(resultSet.getLong(1))
                .firstName(resultSet.getString(2))
                .lastName(resultSet.getString(3))
                .accounts((Set<Account>) resultSet.getObject(4))
                .build();
    }

    private PreparedStatementCallback<Person> getInsertCallBack(Person person) {
        return preparedStatement -> {
            preparedStatement.setLong(1, person.getId());
            preparedStatement.setString(2, person.getFirstName());
            preparedStatement.setString(3, person.getLastName());

            preparedStatement.executeUpdate();

            return person;
        };
    }
}
