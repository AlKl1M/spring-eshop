package com.bfu.feedbackservice.config;

import com.bfu.feedbackservice.client.WebClientCatalogueClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {

    @Bean
    public WebClientCatalogueClient webClientProductsClient(
            @Value("${eshop.services.catalogue.uri:http://localhost:8081}") String catalogueBaseUrl) {
        return new WebClientCatalogueClient(WebClient.builder()
                .filter(new ServletBearerExchangeFilterFunction())
                .baseUrl(catalogueBaseUrl)
                .build());
    }
}