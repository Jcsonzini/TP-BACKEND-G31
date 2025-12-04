package com.tpi.mslogistica.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient catalogoWebClient(
            @Value("${ms.catalogo.base-url}") String baseUrl,
            WebClient.Builder builder
    ) {
        return builder
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public WebClient solicitudesWebClient(
            @Value("${ms.solicitudes.base-url}") String baseUrl,
            WebClient.Builder builder
    ) {
        return builder
                .baseUrl(baseUrl)
                .build();
    }
}

