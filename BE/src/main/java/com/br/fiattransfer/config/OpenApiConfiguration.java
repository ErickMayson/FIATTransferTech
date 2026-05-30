package com.br.fiattransfer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Dev");

        Contact myContact = new Contact();
        myContact.setName("Erick Ferreira");
        myContact.setEmail("ferreiraerick0101@gmail.com");

        Info information = new Info()
                .title("Bank Transfer Scheduler API")
                .version("1.0")
                .description("This API exposes endpoints to Schedule and control Bank Transfers")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}