package net.oleksin.paymentsystem.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Response person entity")
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PersonResponseDto {

  @Schema(description = "person id", example = "44")
  private final Long id;
  @Schema(description = "person first name", example = "John")
  private final String firstName;
  @Schema(description = "person last name", example = "Smith")
  private final String lastName;
}
