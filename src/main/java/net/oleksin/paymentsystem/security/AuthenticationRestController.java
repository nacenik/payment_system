package net.oleksin.paymentsystem.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.oleksin.paymentsystem.exception.JwtAuthenticationException;
import net.oleksin.paymentsystem.exception.UserNotFoundException;
import net.oleksin.paymentsystem.security.jwt.JwtTokenDto;
import net.oleksin.paymentsystem.security.jwt.JwtTokenProvider;
import net.oleksin.paymentsystem.security.user.User;
import net.oleksin.paymentsystem.security.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Auth Controller", description = "auth")
@RequestMapping("api/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationRestController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<JwtTokenDto> login(@RequestBody AuthenticationDto authenticationDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDto.getEmail(), authenticationDto.getPassword()));
        User user = userService.findByEmail(authenticationDto.getEmail());
        String token = jwtTokenProvider.createToken(user);
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        jwtTokenDto.setEmail(user.getEmail());
        jwtTokenDto.setToken(token);
        return ResponseEntity.ok(jwtTokenDto);
    }
}
