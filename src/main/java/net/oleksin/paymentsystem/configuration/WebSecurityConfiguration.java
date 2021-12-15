package net.oleksin.paymentsystem.configuration;

import lombok.RequiredArgsConstructor;
import net.oleksin.paymentsystem.security.ApiAuthenticationEntryPoint;
import net.oleksin.paymentsystem.security.jwt.JwtTokenFilter;
import net.oleksin.paymentsystem.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableGlobalMethodSecurity(prePostEnabled = true,
        securedEnabled = true)
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String BASE_API_URL = "/api/**";
    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
            // other public endpoints of your API may be appended to this array
    };

    private final JwtTokenProvider jwtTokenProvider;
    private ApiAuthenticationEntryPoint apiAuthenticationEntryPoint;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider);
        apiAuthenticationEntryPoint = new ApiAuthenticationEntryPoint();
        jwtTokenFilter.setAuthenticationEntryPoint(apiAuthenticationEntryPoint);
        
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers("/api/auth/login").permitAll()
                .antMatchers("/api/auth/logout").permitAll()
                .antMatchers(BASE_API_URL).authenticated()
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
