package net.oleksin.paymentsystem.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationDto {
    private String email;
    private String username;
    private String password;
}
