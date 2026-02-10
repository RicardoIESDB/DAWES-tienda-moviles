package com.dwes.proyecto.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    // URL de acceso: http://localhost:8080/mobile-store/swagger-ui/index.html

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API tienda de móviles")
                        .version("1.0.0")
                        .description("API REST para la gestión de una tienda de móviles (Proyecto DWES)")
                        .contact(new Contact()
                                .name("Ricardo Leonardo Aucapiña")
                                .email("admin@balmis.com")
                                .url("https://www.balmis.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                        .termsOfService("http://swagger.io/terms/"));
    }
}
