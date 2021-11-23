package net.oleksin.paymentsystem.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Schema(description = "Response person entity", type = "xml")
@Data
@Builder
@ToString
public class PersonResponseDto {

  @Schema(description = "person id", example = "44")
  private final Long id;

  @Schema(description = "person first name", example = "John")
  private final String firstName;

  @Schema(description = "person last name", example = "Smith")
  private final String lastName;
}
