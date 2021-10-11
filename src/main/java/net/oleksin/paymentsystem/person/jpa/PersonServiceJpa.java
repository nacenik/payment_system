package net.oleksin.paymentsystem.person.jpa;

import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.exception.PersonNotFoundException;
import net.oleksin.paymentsystem.person.Person;
import net.oleksin.paymentsystem.person.PersonService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Profile({"springJpaProfile", "default"})
public class PersonServiceJpa implements PersonService {
  private final PersonRepository personRepository;
  
  public PersonServiceJpa(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }
  
  @Transactional
  @Override
  public Person saveNewPerson(Person person) {
    if (person != null) {
      return personRepository.save(person);
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
    return personRepository.findById(id)
            .orElseThrow(PersonNotFoundException::new);
  }

  @Transactional
  @Override
  public Set<Account> getAccountsByPersonId(Long id) {
    Person person = personRepository.findById(id)
            .orElseThrow(PersonNotFoundException::new);
    if (person.getAccounts() == null) {
      return Collections.emptySet();
    }
    return person.getAccounts();
  }
}
