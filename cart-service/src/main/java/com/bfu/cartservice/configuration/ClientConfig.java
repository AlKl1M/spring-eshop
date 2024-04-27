package com.bfu.cartservice.configuration;

import com.bfu.cartservice.client.WebClientCatalogueClient;
import com.bfu.cartservice.security.OAuthClientHttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientConfig {

    @Bean
    public WebClientCatalogueClient productsRestClient(
            @Value("${eshop.services.catalogue.uri:http://localhost:8081}") String catalogueBaseUri,
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository,
            @Value("${eshop.services.catalogue.registration-id:keycloak}") String registrationId,
            LoadBalancerClient loadBalancerClient) {
        return new WebClientCatalogueClient(RestClient.builder()
                .baseUrl(catalogueBaseUri)
                .requestInterceptor(new LoadBalancerInterceptor(loadBalancerClient))
                .requestInterceptor(
                        new OAuthClientHttpRequestInterceptor(
                                new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository,
                                        authorizedClientRepository), registrationId))
                .build());
    }
}