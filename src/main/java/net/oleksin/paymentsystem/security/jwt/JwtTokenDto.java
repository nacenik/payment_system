package net.oleksin.paymentsystem.security.jwt;

import lombok.Data;

@Data
public class JwtTokenDto {

    private String email;
    private String token;
}
