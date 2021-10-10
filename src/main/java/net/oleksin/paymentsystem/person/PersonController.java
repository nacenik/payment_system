package net.oleksin.paymentsystem.person;

import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountDto;
import net.oleksin.paymentsystem.account.AccountResponseDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/person",
        produces = { MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE })
public class PersonController {
  
  private final PersonService personService;
  
  public PersonController(PersonService personService) {
    this.personService = personService;
  }
  
  @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE })
  public PersonResponseDto createPerson(PersonRequestDto personRequestDto) {
    return toPersonDtoAfterSave(personService.saveNewPerson(fromPersonRequestDto(personRequestDto)));
  }
  
  @GetMapping
  public List<PersonResponseDto> getAllPersons() {
    return personService.getAllPersons()
            .stream()
            .map(this::toPersonResponseDto)
            .collect(Collectors.toList());
  }

  @GetMapping(value = "/{id}")
  public PersonResponseDto getPerson(@PathVariable("id") Long id) {
    return toPersonResponseDto(personService.getPersonById(id));
  }
  
  @GetMapping(value = "/{id}/account")
  public AccountResponseDto getAccounts(@PathVariable("id") Long id) {
    return toAccountDto(personService.getAccountByPersonId(id));
  }
  
  private PersonResponseDto toPersonResponseDto(Person person) {
    return PersonResponseDto.builder()
            .id(person.getId())
            .firstName(person.getFirstName())
            .lastName(person.getLastName())
            .build();
  }
  
  private PersonResponseDto toPersonDtoAfterSave(Person person) {
    return PersonResponseDto.builder()
            .id(person.getId())
            .build();
  }
  
  private Person fromPersonRequestDto(PersonRequestDto requestDto) {
    return Person.builder()
            .firstName(requestDto.getFirstName())
            .lastName(requestDto.getLastName())
            .accounts(requestDto
                    .getAccounts()
                    .stream()
                    .map(this::fromAccountRequestDto)
                    .collect(Collectors.toSet()))
            .build();
  }
  
  private Account fromAccountRequestDto(AccountDto accountDto) {
    return Account.builder()
            .accountNumber(accountDto.getAccountNumber())
            .balance(accountDto.getBalance())
            .build();
  }
  
  private AccountResponseDto toAccountDto(Set<Account> accounts) {
    return AccountResponseDto.builder()
            .accounts(accounts
                    .stream()
                    .map(this::toAccountRequestDto)
                    .collect(Collectors.toSet()))
            .build();
  }
  
  private AccountDto toAccountRequestDto(Account account) {
    return AccountDto.builder()
            .id(account.getId())
            .accountNumber(account.getAccountNumber())
            .accountType(account.getAccountType().getName())
            .balance(account.getBalance())
            .build();
  }
}
