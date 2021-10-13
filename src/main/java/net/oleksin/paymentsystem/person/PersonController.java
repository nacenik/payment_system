package net.oleksin.paymentsystem.person;

import net.oleksin.paymentsystem.Converter;
import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/persons",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class PersonController {
  
  private final PersonService personService;
  private final Converter<AccountDto, AccountDto, Account> accountConverter;
  private final Converter<PersonRequestDto, PersonResponseDto, Person> personConverter;
  
  public PersonController(PersonService personService, Converter<AccountDto, AccountDto, Account> accountConverter, Converter<PersonRequestDto, PersonResponseDto, Person> personConverter) {
    this.personService = personService;
    this.accountConverter = accountConverter;
    this.personConverter = personConverter;
  }
  
  @PostMapping
  public PersonResponseDto createPerson(PersonRequestDto personRequestDto) {
    Person person = personService.saveNewPerson(personConverter.fromRequestDto(personRequestDto));
    return personConverter.toResponseDto(person);
  }
  
  @GetMapping
  public List<PersonResponseDto> getAllPersons() {
    return personService.getAllPersons()
            .stream()
            .map(personConverter::toResponseDto)
            .collect(Collectors.toList());
  }

  @GetMapping(value = "/{id}")
  public PersonResponseDto getPersonById(@PathVariable("id") Long id) {
    return personConverter.toResponseDto(personService.getPersonById(id));
  }
  
  @GetMapping(value = "{id}/accounts")
  public Set<AccountDto> getAccountsByPersonId(@PathVariable("id") Long id) {
    return personService.getAccountsByPersonId(id).stream()
            .map(accountConverter::toResponseDto)
            .collect(Collectors.toSet());
  }
}
