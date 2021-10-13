package net.oleksin.paymentsystem.person;

import net.oleksin.paymentsystem.Converter;
import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PersonConverter implements Converter<PersonRequestDto, PersonResponseDto, Person> {
  
  private final Converter<AccountDto, AccountDto, Account> accountConverter;
  
  public PersonConverter(Converter<AccountDto, AccountDto, Account> accountConverter) {
    this.accountConverter = accountConverter;
  }
  
  @Override
  public PersonResponseDto toResponseDto(Person person) {
    return PersonResponseDto.builder()
            .id(person.getId())
            .firstName(person.getFirstName())
            .lastName(person.getLastName())
            .build();
  }
  
  @Override
  public Person fromRequestDto(PersonRequestDto personRequestDto) {
    return Person.builder()
            .id(personRequestDto.getId())
            .firstName(personRequestDto.getFirstName())
            .lastName(personRequestDto.getLastName())
            .accounts(personRequestDto.getAccounts().stream()
                    .map(accountConverter::fromRequestDto)
                    .collect(Collectors.toSet()))
            .build();
            
  }
}
