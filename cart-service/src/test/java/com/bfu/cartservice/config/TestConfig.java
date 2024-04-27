package com.bfu.cartservice.config;

import com.bfu.cartservice.client.WebClientCatalogueClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.MongoDBContainer;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ServiceConnection
    public MongoDBContainer mongoDBContainer() {
        return new MongoDBContainer("mongo:7");
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return mock();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return mock();
    }

    @Bean
    @Primary
    public WebClientCatalogueClient testWebClientCatalogueClient(
            @Value("${eshop.services.catalogue.uri:http://localhost:8081}") String catalogueBaseUri
    ) {
        return new WebClientCatalogueClient(RestClient.builder()
                .baseUrl(catalogueBaseUri)
                .build());
    }
}
