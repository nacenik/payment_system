package net.oleksin.paymentsystem.exception;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@JsonDeserialize(builder = ExceptionDto.ExceptionDtoBuilder.class)
@XmlRootElement
public class ExceptionDto {
    private String message;
    private int status;
}
