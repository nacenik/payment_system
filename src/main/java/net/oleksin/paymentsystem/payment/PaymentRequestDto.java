package net.oleksin.paymentsystem.payment;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@Schema(description = "Request payment entity")
@JsonDeserialize(builder = PaymentRequestDto.PaymentRequestDtoBuilder.class)
@XmlRootElement
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto implements Serializable {

  @Schema(description = "source account id", example = "2")
  private Long sourceAccountId;
  @Schema(description = "destination account id", example = "3")
  private Long destinationAccountId;
  @Schema(description = "amount", example = "500.00")
  private BigDecimal amount;
  @Schema(description = "payment reason", example = "for example")
  private String reason;

  @JsonPOJOBuilder(withPrefix = "")
  public static class PaymentRequestDtoBuilder {
  }
}
