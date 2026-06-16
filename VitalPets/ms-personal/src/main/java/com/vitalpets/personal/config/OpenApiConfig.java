package com.vitalpets.personal.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI personalOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("VitalPets — MS-Personal API")
                .description("Gestión de veterinarios, estilistas y técnicos.")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Leandro Ruiz")
                    .email("leandrorafaelruizruiz@gmail.com"))
                .license(new License()
                    .name("Proyecto Académico — DuocUC")
                    .url("https://github.com/donMixho/VitalPets")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8087")
                    .description("Servidor local de desarrollo")));
    }
}
