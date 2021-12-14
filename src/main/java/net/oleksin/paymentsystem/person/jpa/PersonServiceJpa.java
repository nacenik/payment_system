package net.oleksin.paymentsystem.person.jpa;

import lombok.AllArgsConstructor;
import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountService;
import net.oleksin.paymentsystem.exception.PersonNotFoundException;
import net.oleksin.paymentsystem.person.Person;
import net.oleksin.paymentsystem.person.PersonService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Profile({ "default", "springJpaProfile"})
@AllArgsConstructor
public class PersonServiceJpa implements PersonService {
  private final PersonRepository personRepository;
  private final AccountService accountService;

  @Transactional
  @Override
  public Person saveNewPerson(Person person) {
    if (person != null && person.getAccounts() != null) {
      Person newPerson = personRepository.save(person);
      person.getAccounts().forEach(account -> {
        account.setPerson(newPerson);
        accountService.saveNewAccount(account);
      });
      return newPerson;
    }
    return null;
  }
  
  @Transactional
  @Override
  public List<Person> getAllPersons() {
    return personRepository.findAll();
  }

  @Transactional
  @Override
  public Person getPersonById(Long id) {
    return getPerson(id);
  }

  @Transactional
  @Override
  public List<Account> getAccountsByPersonId(Long id) {
    Person person = getPersonById(id);
    if (person.getAccounts() == null) {
      return Collections.emptyList();
    }
    return person.getAccounts();
  }

  @Override
  public boolean existByPersonIdAndAccountId(Long personId, Long accountId) {
    return personRepository.existsByIdAndAccountsId(personId, accountId);
  }

  private Person getPerson(Long id) {
    return personRepository.findById(id)
            .orElseThrow(() -> new PersonNotFoundException(String.format("Person with id = %d not found", id)));
  }
}
