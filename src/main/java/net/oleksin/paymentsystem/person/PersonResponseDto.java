package net.oleksin.paymentsystem.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class PersonResponseDto {
  private final Long id;
  private final String firstName;
  private final String lastName;
}
