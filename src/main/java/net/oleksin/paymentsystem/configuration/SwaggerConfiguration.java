package net.oleksin.paymentsystem.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(
                new Info()
                        .title("Simple Payment System API")
                        .version("1.0.0")
                        .contact(
                                new Contact()
                                        .name("Mykyta")
                                        .email("nacenik@gmail.com")
                                        .url("https://github.com/nacenik?tab=repositories")
                        )
        );
    }
}
