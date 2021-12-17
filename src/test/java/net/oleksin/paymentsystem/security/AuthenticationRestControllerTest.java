package net.oleksin.paymentsystem.security;

import net.oleksin.paymentsystem.exception.PaymentNotFoundException;
import net.oleksin.paymentsystem.exception.UserNotFoundException;
import net.oleksin.paymentsystem.security.jwt.JwtTokenDto;
import net.oleksin.paymentsystem.security.jwt.JwtTokenProvider;
import net.oleksin.paymentsystem.security.jwt.UserDetailsServiceImpl;
import net.oleksin.paymentsystem.security.user.User;
import net.oleksin.paymentsystem.security.user.UserService;
import net.oleksin.paymentsystem.security.user.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationRestControllerTest {

    private static final String TEST = "test";
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private AuthenticationRestController authenticationRestController;

    @Test
    void loginSuccessfulTest() {
        AuthenticationDto authenticationDto = AuthenticationDto.builder()
                .email("test@mail.com")
                .password(TEST)
                .username(TEST)
                .build();
        User user = User.builder()
                .id(1L)
                .email(authenticationDto.getEmail())
                .username(authenticationDto.getUsername())
                .password(authenticationDto.getPassword())
                .roles(List.of(Role.builder()
                        .id(1L)
                        .roleName("ROLE_ADMIN")
                        .build()))
                .build();
        when(userService.findByEmail(anyString())).thenReturn(user);
        when(jwtTokenProvider.createToken(any())).thenReturn(authenticationDto.toString());

        ResponseEntity<?> responseEntity = authenticationRestController.login(authenticationDto);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof JwtTokenDto);
        JwtTokenDto jwtTokenDto = (JwtTokenDto) responseEntity.getBody();
        assertEquals(authenticationDto.toString(), jwtTokenDto.getToken());
        assertEquals(authenticationDto.getEmail(), jwtTokenDto.getEmail());
        verify(authenticationManager).authenticate(any());
        verify(userService).findByEmail(anyString());
        verify(jwtTokenProvider).createToken(any());
    }


}