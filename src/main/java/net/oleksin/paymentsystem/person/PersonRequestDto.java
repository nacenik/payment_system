package net.oleksin.paymentsystem.person;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.oleksin.paymentsystem.account.AccountDto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Schema(description = "Request person entity")
@JsonDeserialize(builder = PersonRequestDto.PersonRequestDtoBuilder.class)
@XmlRootElement
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonRequestDto  {

  @Schema(description = "person first name", example = "John")
  private String firstName;

  @Schema(description = "person last name", example = "Smith")
  private String lastName;

  @Schema(description = "array of person accounts")
  private List<AccountDto> accounts;

  @JsonPOJOBuilder(withPrefix = "")
  public static class PersonRequestDtoBuilder {
  }
}
