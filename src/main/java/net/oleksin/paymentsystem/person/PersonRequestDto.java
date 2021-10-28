package net.oleksin.paymentsystem.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.oleksin.paymentsystem.account.AccountDto;

import java.util.Set;

@Schema(description = "Request person entity")
@AllArgsConstructor
@Builder
@Setter
@Getter
public class PersonRequestDto {

  @Schema(description = "person first name", example = "John")
  private final String firstName;
  @Schema(description = "person last name", example = "Smith")
  private final String lastName;
  @Schema(description = "array of person accounts")
  private final Set<AccountDto> accounts;
}
