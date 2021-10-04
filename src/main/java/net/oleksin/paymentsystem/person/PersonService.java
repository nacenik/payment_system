package net.oleksin.paymentsystem.person;

import net.oleksin.paymentsystem.account.Account;

import java.util.List;
import java.util.Set;

public interface PersonService {
  Person saveNewPerson(Person person);
  
  List<Person> getAllPersons();
  
  Person getPersonById(Long id);
  
  Set<Account> getAccountByPersonId(Long id);
}
