package net.oleksin.paymentsystem.person;

import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountDto;
import net.oleksin.paymentsystem.account.AccountResponseDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(name = "/person",
        produces = { MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE })
public class PersonController {
  
  private final PersonService personService;
  
  public PersonController(PersonService personService) {
    this.personService = personService;
  }
  
  @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE })
  public PersonDto createPerson(PersonRequestDto personRequestDto) {
    return toPersonResponseDto(personService.saveNewPerson(fromPersonRequestDto(personRequestDto)));
  }
  
  @GetMapping(name = "/{id}",
          consumes = { MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE })
  public AccountResponseDto getAccounts(@PathVariable("id") PersonDto personDto) {
    return toAccountDto(personService.getAccountByPersonId(personDto.getId()));
  }
  
  private PersonDto toPersonResponseDto(Person person) {
    return PersonDto.builder()
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
            .accountType(accountDto.getAccountType())
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
            .accountType(account.getAccountType())
            .balance(account.getBalance())
            .build();
  }
}
