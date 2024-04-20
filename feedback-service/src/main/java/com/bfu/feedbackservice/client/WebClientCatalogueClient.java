package com.bfu.feedbackservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class WebClientCatalogueClient implements CatalogueClient{
    private final WebClient webClient;

}
