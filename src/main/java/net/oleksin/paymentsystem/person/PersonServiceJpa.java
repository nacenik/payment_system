package net.oleksin.paymentsystem.person;

import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Profile({"springJpaProfile", "default"})
public class PersonServiceJpa implements PersonService {
  private final PersonRepository personRepository;
  private final AccountRepository accountRepository;
  
  public PersonServiceJpa(PersonRepository personRepository, AccountRepository accountRepository) {
    this.personRepository = personRepository;
    this.accountRepository = accountRepository;
  }
  
  @Override
  public Long saveNewPerson(Person person) {
    if (person != null) {
      return personRepository.save(person).getId();
    }
    return null;
  }
  
  @Override
  public List<Person> getAllPersons() {
    return personRepository.findAll();
  }
  
  @Override
  public Person getPersonById(Long id) {
    return personRepository.getById(id);
  }
  
  @Override
  public Set<Account> getAccountByPersonId(Long id) {
    return personRepository.getById(id).getAccounts();
  }
}
