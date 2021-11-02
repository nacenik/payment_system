package net.oleksin.paymentsystem.person;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.oleksin.paymentsystem.Converter;
import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Person Controller", description = "Get or create persons and get all person's accounts")
@RestController
@RequestMapping(value = "/api/persons",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@AllArgsConstructor
public class PersonController {
  
  private final PersonService personService;
  private final Converter<AccountDto, AccountDto, Account> accountConverter;
  private final Converter<PersonRequestDto, PersonResponseDto, Person> personConverter;

  @Operation(summary = "Create new person")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "201",
                  description = "Creating a person with accounts",
                  content = {
                          @Content(
                                  mediaType = "application/json",
                                  array = @ArraySchema(schema = @Schema(implementation = PersonResponseDto.class))),
                          @Content(
                                  mediaType = "application/xml",
                                  array = @ArraySchema(schema = @Schema(implementation = PersonResponseDto.class))),
  })
})
  @PostMapping
  public ResponseEntity<Object> createPerson(@Parameter(description = "Information about new person") @Valid @RequestBody PersonRequestDto personRequestDto) {
    Person person = personService.saveNewPerson(personConverter.fromRequestDto(personRequestDto));
    return ResponseEntity.status(201).body(personConverter.toResponseDto(person));
  }

  @Operation(summary = "Get all persons")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "200",
                  description = "Getting information about all existing persons",
                  content = {
                          @Content(
                                  mediaType = "application/json",
                                  array = @ArraySchema(schema = @Schema(implementation = PersonResponseDto.class))),
                          @Content(
                                  mediaType = "application/xml",
                                  array = @ArraySchema(schema = @Schema(implementation = PersonResponseDto.class))),
                  })
  })
  @GetMapping
  public List<PersonResponseDto> getAllPersons() {
    return personService.getAllPersons()
            .stream()
            .map(personConverter::toResponseDto)
            .collect(Collectors.toList());
  }

  @Operation(summary = "Get person by id")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "200",
                  description = "Getting information about a specific person",
                  content = {
                          @Content(
                                  mediaType = "application/json",
                                  array = @ArraySchema(schema = @Schema(implementation = PersonResponseDto.class))),
                          @Content(
                                  mediaType = "application/xml",
                                  array = @ArraySchema(schema = @Schema(implementation = PersonResponseDto.class))),
                  })
  })
  @GetMapping(value = "/{id}")
  public PersonResponseDto getPersonById(@Parameter(description = "Person id") @PathVariable("id") Long id) {
    return personConverter.toResponseDto(personService.getPersonById(id));
  }

  @Operation(summary = "Get accounts by person id")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "200",
                  description = "Getting all person's accounts",
                  content = {
                          @Content(
                                  mediaType = "application/json",
                                  array = @ArraySchema(schema = @Schema(implementation = PersonResponseDto.class))),
                          @Content(
                                  mediaType = "application/xml",
                                  array = @ArraySchema(schema = @Schema(implementation = PersonResponseDto.class))),
                  })
  })
  @GetMapping(value = "{id}/accounts")
  public List<AccountDto> getAccountsByPersonId(@Parameter(description = "Person id") @PathVariable("id") Long id) {
    return personService.getAccountsByPersonId(id).stream()
            .map(accountConverter::toResponseDto)
            .collect(Collectors.toList());
  }
}
