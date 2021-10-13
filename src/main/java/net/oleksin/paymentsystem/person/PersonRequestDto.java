package net.oleksin.paymentsystem.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.oleksin.paymentsystem.account.AccountDto;

import java.util.Set;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class PersonRequestDto {
  
  private Long id;
  private final String firstName;
  private final String lastName;
  private final Set<AccountDto> accounts;
}
