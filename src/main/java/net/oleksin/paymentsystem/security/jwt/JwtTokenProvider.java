package net.oleksin.paymentsystem.security.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import net.oleksin.paymentsystem.exception.JwtAuthenticationException;
import net.oleksin.paymentsystem.security.user.User;
import net.oleksin.paymentsystem.security.user.role.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private String secretKey;

    private final SecurityConfiguration.JwtConfigurationProperties jwtConfigurationProperties;
    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(jwtConfigurationProperties.getSecret().getBytes());
    }

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("roles", getRolesName(user));
        Date date = new Date();
        Date validity = new Date(date.getTime() + jwtConfigurationProperties.getTtl() * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) throws JwtAuthenticationException {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer")) {
            return bearer.replace("Bearer", "").trim();
        }
        return null;
    }

    private List<String> getRolesName(User user) {
        return user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());
    }
}
