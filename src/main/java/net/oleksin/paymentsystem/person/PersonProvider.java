package net.oleksin.paymentsystem.person;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.oleksin.paymentsystem.account.Account;
import net.oleksin.s3starter.client.S3ClientWorker;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class PersonProvider {

    private final PersonService personService;
    private final S3ClientWorker s3ClientWorker;

    public Person createPerson(Person person) {
        Person newPerson = personService.saveNewPerson(person);
        try {
            s3ClientWorker.putObject(new ByteArrayInputStream(newPerson.toString().getBytes(StandardCharsets.UTF_8)),
                    newPerson.getClass().getSimpleName(), newPerson.getId());
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        s3ClientWorker.listObjects().forEach(log::info);

        return newPerson;
    }

    public List<Person> getAllPersons() {
        List<Person> personList = personService.getAllPersons();
        personList.forEach(person -> {
            try {
                s3ClientWorker.putObject(new ByteArrayInputStream(person.toString().getBytes(StandardCharsets.UTF_8)),
                        person.getClass().getSimpleName(), person.getId());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        });
        s3ClientWorker.listObjects().forEach(log::info);

        return personList;
    }

    public Person getPersonById(Long id) {
        Person person = personService.getPersonById(id);
        try {
            s3ClientWorker.putObject(new ByteArrayInputStream(person.toString().getBytes(StandardCharsets.UTF_8)),
                    person.getClass().getSimpleName(), person.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }

        s3ClientWorker.listObjects().forEach(log::info);
        return person;
    }

    public List<Account> getAccountsByPersonId(Long id) {
        List<Account> accounts = personService.getAccountsByPersonId(id);

        accounts.forEach(account -> {
            try {
                s3ClientWorker.putObject(new ByteArrayInputStream(account.toString().getBytes(StandardCharsets.UTF_8)),
                        account.getClass().getSimpleName(), account.getId());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        });
        s3ClientWorker.listObjects().forEach(log::info);

        return accounts;
    }
}
