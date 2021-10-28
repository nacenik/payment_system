package net.oleksin.paymentsystem.person;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.oleksin.paymentsystem.Converter;
import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "Person Controller", description = "Get or create persons and get all person's accounts")
@RestController
@RequestMapping(value = "/api/persons",
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

  @Operation(summary = "Create new person",
          description = "For creating new person with accounts")
  @PostMapping
  public PersonResponseDto createPerson(@Parameter(description = "Information about new person") @RequestBody PersonRequestDto personRequestDto) {
    Person person = personService.saveNewPerson(personConverter.fromRequestDto(personRequestDto));
    return personConverter.toResponseDto(person);
  }

  @Operation(summary = "Get all persons",
          description = "For getting information about all existing persons")
  @GetMapping
  public List<PersonResponseDto> getAllPersons() {
    return personService.getAllPersons()
            .stream()
            .map(personConverter::toResponseDto)
            .collect(Collectors.toList());
  }

  @Operation(summary = "Get person by id",
          description = "For getting information about a specific person")
  @GetMapping(value = "/{id}")
  public PersonResponseDto getPersonById(@Parameter(description = "Person id") @PathVariable("id") Long id) {
    return personConverter.toResponseDto(personService.getPersonById(id));
  }

  @Operation(summary = "Get accounts by person id",
          description = "For getting all person's accounts")
  @GetMapping(value = "{id}/accounts")
  public Set<AccountDto> getAccountsByPersonId(@Parameter(description = "Person id") @PathVariable("id") Long id) {
    return personService.getAccountsByPersonId(id).stream()
            .map(accountConverter::toResponseDto)
            .collect(Collectors.toSet());
  }
}
