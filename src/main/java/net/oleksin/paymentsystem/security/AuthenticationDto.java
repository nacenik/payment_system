package net.oleksin.paymentsystem.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationDto {
    private String email;
    private String username;
    private String password;
}
