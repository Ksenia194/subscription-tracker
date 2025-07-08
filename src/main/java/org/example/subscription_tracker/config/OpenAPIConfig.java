package org.example.subscription_tracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Subscription Tracker API")
                        .version("1.0.0")
                        .description("API to manage user subscriptions: create, update, view and delete.")
                        .contact(new Contact()
                                .name("Ksenia")
                                .email("sakavchak.dev@gmail.com")
                        )
                );
    }
}
