package net.oleksin.paymentsystem.person;

import net.oleksin.paymentsystem.account.Account;

import java.util.List;

public interface PersonService {
  Person saveNewPerson(Person person);
  
  List<Person> getAllPersons();
  
  Person getPersonById(Long id);
  
  List<Account> getAccountsByPersonId(Long id);
}
