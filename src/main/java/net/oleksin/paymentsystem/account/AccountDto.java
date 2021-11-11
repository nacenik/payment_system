package net.oleksin.paymentsystem.account;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@Schema(description = "Account entity")
@JsonDeserialize(builder = AccountDto.AccountDtoBuilder.class)
@XmlRootElement
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto implements Serializable {

  @Schema(hidden = true)
  private Long id;

  @Schema(description = "account number", example = "1232312A2")
  private String accountNumber;

  @Schema(description = "account type", example = "simple/card")
  private String accountType;

  @Schema(description = "amount", example = "5000")
  private BigDecimal balance;

  @JsonPOJOBuilder(withPrefix = "")
  public static class AccountDtoBuilder {
  }
}
