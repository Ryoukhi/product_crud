package com.eadl.product_management.config;

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
    public OpenAPI productApiOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Serveur de développement");
        
        Contact contact = new Contact();
        contact.setEmail("stephen.deutou@gmail.com");
        contact.setName("Steph DevOps");
        contact.setUrl("https://www.stephdeutou.com");
        
        License license = new License()
                .name("IUC License")
                .url("https://kmergenius.com/licenses/iuc/");
        
        Info info = new Info()
                .title("API de Gestion de Produits")
                .version("1.0.0")
                .contact(contact)
                .description("Cette API permet de gérer un catalogue de produits avec des opérations CRUD complètes.")
                .termsOfService("https://www.iuc.com/terms")
                .license(license);
        
        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}